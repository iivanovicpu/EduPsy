import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.iivanovic.psyedu.controllers.AdaptiveRuleTypes;
import hr.iivanovic.psyedu.db.Subject;
import hr.iivanovic.psyedu.db.SubjectPosition;

/**
 * @author iivanovic
 * @date 23.11.16.
 */
public class EtcTest {
    public static void main(String[] args) {
        EtcTest test = new EtcTest();
        test.testGrouping();
    }

    private void testGrouping(){
        List<Subject> list = new LinkedList<>();
        list.add(new Subject(1,"Predmet 1","","",1,1,0,0,"","",1, SubjectPosition.PREDMET));
        list.add(new Subject(2,"Cjelina 1","","",1,1,0,0,"","",2, SubjectPosition.CJELINA));
        list.add(new Subject(3,"Cjelina 2","","",1,1,0,0,"","",2, SubjectPosition.CJELINA));
        list.add(new Subject(4,"Jedinica 1","","",2,1,0,0,"","",3, SubjectPosition.JEDINICA));
        list.add(new Subject(5,"Čestica 1","","",4,1,0,0,"","",4, SubjectPosition.CESTICA));
        list.add(new Subject(6,"Jedinica 2","","",3,1,0,0,"","",3, SubjectPosition.JEDINICA));
        list.add(new Subject(7,"Čestica 2","","",6,1,0,0,"","",4, SubjectPosition.CESTICA));
//        list.forEach(System.out::println);

//        Map<Integer, List<Subject>> groupping = list.stream()
//                .collect(Collectors.groupingBy(Subject::getId,
//                        Collectors.mapping(Subject::getSubjectId, Collectors.toList())));
//
/*
        Map<Integer, Map<Integer, List<Subject>>> grouping = list.stream().collect(Collectors.groupingBy(Subject::getId,
                Collectors.groupingBy(Subject::getSubjectId)));
        grouping.forEach((integer, integerListMap) -> System.out.println(integer + " " + integerListMap));
*/

        // mape subjectId s listom svih njegovih childova
        Map<Integer, List<Subject>> groupingBySubjectId = list.stream().filter(s -> s.getId() != s.getSubjectId()).collect(Collectors.groupingBy(Subject::getSubjectId));
        groupingBySubjectId.forEach((integer, integerListMap) -> System.out.println(integer + " " + integerListMap));
        System.out.println("--------- prvo provjeri postoji li slijedeći subject koji ima subjectId trenutnog");
        System.out.println(groupingBySubjectId.get(1).stream().findFirst().orElse(null));
        System.out.println(groupingBySubjectId.get(2).stream().findFirst().orElse(null));
        System.out.println(groupingBySubjectId.get(3).stream().findFirst().orElse(null));
        System.out.println(groupingBySubjectId.get(4).stream().findFirst().orElse(null));
//        System.out.println(grouping1.get(5).stream().filter(s -> s.getId() != 5).findFirst().orElse(null));           // 5 nema child-a
        System.out.println(groupingBySubjectId.get(6).stream().findFirst().orElse(null));
        System.out.println("--------- ako nema uzmi slijedeći koji ima isti parentSubjectId");

        // mapa subjectId s listom subjecta kojima je on parent
        Map<Integer, List<Subject>> groupingByParentSubjectId = list.stream().collect(Collectors.groupingBy(Subject::getParentSubjectId));
        groupingByParentSubjectId.forEach((integer, integerListMap) -> System.out.println(integer + " " + integerListMap));
        System.out.println(groupingByParentSubjectId.get(1).stream().filter(s -> s.getId() > 5).findFirst().orElse(null));  // slijedeći od 5 je 6
        System.out.println(groupingByParentSubjectId.get(1).stream().filter(s -> s.getId() > 7).findFirst().orElse(null));  // nema slijedećeg od 7

    }

    private static void oznake() {
        String marks = "<script>var oznake = [";
        for (AdaptiveRuleTypes ruleType : AdaptiveRuleTypes.values()) {
            System.out.println(ruleType);
            System.out.println(ruleType.getDescription());
            System.out.println(ruleType.getMark());
            marks = marks.concat("'").concat(ruleType.getMark()).concat("',");
        }
        marks = marks.substring(0, marks.length()-1).concat("];</script>");
        System.out.println(marks);
    }
}
