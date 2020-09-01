import java.util.Stack;

public class InterpreterPatternGensql {
    //输入的格式按照SQL语句形式，而且每条表达式均用()括起来。
    //!符号放在括号前，无需另外添加括号
    //and/or符号前后存在空格,均采用小写。
    //输入的格式严格按照上述条件，没有进行格式的检查。
    //只暴露一个连接符在外。如(a = 1) or ((b=2) and (c=3))
    public static String gensql(String expression){
        //！符号放在括号栈中，要是其随后的一个括号被弹出，则一起弹出放在表达式中
        Stack<String> bracket = new Stack<String>();
        Stack<String> operation = new Stack<String>();
        Stack<String> expr = new Stack<String>();
        String prefix = "select * from apink where ";
        String sql = "";
        String tempexpr;
        String tempexpr1;
        String tempbracket;
        String tempoper;

        int begin = 0;
        //去除参数字符串所有空格
        expression = expression.replace(" ","");
        for (int i=0;i<expression.length();i++){
            char t = expression.charAt(i);
            if (t=='('){
                if(!bracket.isEmpty() && bracket.peek().equals("!")){
                    bracket.pop();
                    bracket.push("!(");
                }else {
                    bracket.push(Character.toString(t));
                }
                if(i+1<expression.length() && Character.isLetterOrDigit(expression.charAt(i+1))){
                    begin = i+1;
                    while(expression.charAt(i) !=')'){
                        i++;
                    }
                    expr.push(expression.substring(begin,i));
                    i=i-1;

                }
            }
            if(t==')'){
                if(bracket.isEmpty()){
                    throw new IllegalArgumentException("括号不匹配");
                }else {
                    if(expr.isEmpty()){
                        bracket.pop();
                        throw new IllegalArgumentException("这个括号里没有表达式！！！");
                    }else{
                        tempbracket=bracket.pop();
                        tempexpr = expr.pop();
                        if(tempexpr.contains(")")){
                            tempoper = operation.pop();
                            tempexpr1 = expr.pop();
                            tempexpr = combineSQL(tempexpr1,tempexpr,tempoper);

                        }
                            expr.push(tempbracket + tempexpr + ")");
                    }
                }
                if(i+1<expression.length() && "ao".contains(expression.substring(i+1,i+2))){
                    if(expression.substring(i+1,i+4).equals("and")){
                        operation.push(expression.substring(i+1,i+4));
                        i=i+3;
                    }else if(expression.substring(i+1,i+3).equals("or")){
                        operation.push(expression.substring(i+1,i+3));
                        i=i+2;
                    }else{
                        System.out.println("连接符有问题！");
                    }
                }

            }
            if(t=='!'){
                bracket.push(Character.toString(t));
            }
        }
        while(!operation.isEmpty()){
            tempoper = operation.pop();
            tempexpr = expr.pop();
            tempexpr1 = expr.pop();
            tempexpr= combineSQL(tempexpr1,tempexpr,tempoper);
            if(operation.isEmpty()){
                expr.push(tempexpr);
            }else{
            expr.push("("+tempexpr+")");}
        }
        sql = expr.pop();
        return prefix+sql;

    }

    static String combineSQL(String expr1,String expr2,String operation){
        String temp="";
        Expression expersion1 = new TerminalExpression(expr1);
        Expression expersion2 = new TerminalExpression(expr2);
        if(operation.equals("and")){
            temp = new AndExpression(expersion1,expersion2).interpret();
        }
        if(operation.equals("or")){
            temp = new OrExpression(expersion1,expersion2).interpret();
        }
        return temp;
    }




}
