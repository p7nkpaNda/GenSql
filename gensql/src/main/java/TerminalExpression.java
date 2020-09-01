public class TerminalExpression implements Expression {
    private String data;

    public TerminalExpression(String data){
        this.data = data;
    }

    @Override
    //之后要是需要将程序化语句和SQL语句处理，可以在这个函数处理。
    public String interpret() {
        return data;
    }
}
