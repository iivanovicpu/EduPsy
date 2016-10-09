package hr.iivanovic.psyedu.controllers;

import static hr.iivanovic.psyedu.util.RequestUtil.clientAcceptsHtml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.iivanovic.psyedu.db.Question;
import hr.iivanovic.psyedu.db.TitleLearningStatus;
import hr.iivanovic.psyedu.db.User;
import hr.iivanovic.psyedu.util.Path;
import hr.iivanovic.psyedu.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author iivanovic
 * @date 09.10.16.
 */
public class ExamController extends AbstractController {

    public static Route fetchQuestionsForTitle = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        int subjectid = Integer.parseInt(request.params("subjectid"));
        String titleid = request.params("titleid");
        User student = LoginController.getCurrentUser(request);
        if (clientAcceptsHtml(request)) {
            Map<String, Object> model = new HashMap<>();
            List<Question> questions = dbProvider.getAllQuestionsForSubjectAndTitle(subjectid, titleid);
            String htmlQuestions = "ispitna pitanja za ovo gradivo nisu unesena !";
            if(questions.size() > 0){
                 htmlQuestions = renderQuestions(questions);
            }
            model.put("questions", htmlQuestions);
            model.put("subjectId", subjectid);
            model.put("titleId", titleid);
            dbProvider.logLearningStatus(student.getId(),subjectid,titleid, TitleLearningStatus.OPENED_EXAM.getId());
            return ViewUtil.render(request, model, Path.Template.EXAM);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
