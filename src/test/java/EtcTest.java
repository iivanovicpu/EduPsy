import hr.iivanovic.psyedu.controllers.AdaptiveRuleTypes;

/**
 * @author iivanovic
 * @date 23.11.16.
 */
public class EtcTest {
    public static void main(String[] args) {
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
