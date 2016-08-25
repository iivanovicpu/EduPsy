package hr.iivanovic.psyedu;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hr.iivanovic.psyedu.util.JsonUtil;
import lombok.Data;

public class BlogService {

    private static final int HTTP_BAD_REQUEST = 400;

    interface Validable {
        boolean isValid();
    }

    @Data
    static class NewPostPayload {
        private String title;
        private List categories = new LinkedList<String>();
        private String content;

        public boolean isValid() {
            return title != null && !title.isEmpty() && !categories.isEmpty();
        }
    }

    // In a real application you may want to use a DB, for this example we just store the posts in memory
    public static class Model {
        private int nextId = 1;
        private Map posts = new HashMap<Integer, String>();

        @Data
        class Post {
            private int id;
            private String title;
            private List categories;
            private String content;
        }

        public int createPost(String title, String content, List categories) {
            int id = nextId++;
            Post post = new Post();
            post.setId(id);
            post.setTitle(title);
            post.setContent(content);
            post.setCategories(categories);
            posts.put(id, post);
            return id;
        }

        public List getAllPosts() {
            return (List) posts.keySet().stream().sorted().map((id) -> posts.get(id)).collect(Collectors.toList());

        }

    }

    public static void main(String[] args) {
        Model model = new Model();

        // insert a post (using HTTP post method)
        post("/posts", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NewPostPayload creation = mapper.readValue(request.body(), NewPostPayload.class);
                if (!creation.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                int id = model.createPost(creation.getTitle(), creation.getContent(), creation.getCategories());
                response.status(200);
                response.type("application/json");
                return id;
            } catch (JsonParseException jpe) {
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
        });

        // get all post (using HTTP get method)
        get("/posts", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return JsonUtil.dataToJson(model.getAllPosts());
        });
    }
}