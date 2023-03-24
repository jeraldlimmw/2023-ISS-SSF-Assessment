package ibf2022.batch2.ssf.frontcontroller.models;

import java.io.Serializable;
// import java.util.Random;
import java.util.Random;

// import jakarta.json.Json;
// import jakarta.json.JsonObject;
import jakarta.validation.constraints.Size;

public class Login implements Serializable{
    
    @Size(min=2, message="username must be at least 2 characters")
    private String username;
    
    @Size(min=2, message="password must be at least 2 characters")
    private String password;
    private int failed = 0;

    private boolean captcha = false;

    private final String[] OPERATORS = {"+", "-", "x", "/"};
    private int firstNum;
    private int secondNum;
    private int answer;
    private String question;
    private String useranswer;
    private boolean authenticated = false;

    public Login() {
        this.failed = failed;
        this.captcha = captcha;
        this.question = this.generateCaptcha();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getFailed() {
        return failed;
    }
    public void setFailed(int failed) {
        this.failed = failed;
    }
    public boolean isCaptcha() {
        return captcha;
    }
    public void setCaptcha(boolean captcha) {
        this.captcha = captcha;
    }
    public String getUseranswer() {
        return useranswer;
    }
    public int getAnswer() {
        return answer;
    }
    public boolean isAuthenticated() {
        return authenticated;
    }
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getQuestion() {
        return question;
    }

    public String generateCaptcha(){
        Random rand = new Random();
        firstNum = rand.nextInt(50)+1;
        secondNum = rand.nextInt(50)+1;
        String op = OPERATORS[rand.nextInt(4)];

        float floatFirstNum = (float) firstNum;
        float floatAnswer = -1f;
        switch(op){
            case "+" :
                floatAnswer = floatFirstNum + secondNum;
                break;
            case "-" :
                floatAnswer = floatFirstNum - secondNum;
                break;
            case "x" :
                floatAnswer = floatFirstNum * secondNum;
                break;
            case "/" :
                floatAnswer = floatFirstNum / secondNum;
                break;
        }
        answer = (int) Math.round(floatAnswer);

        return "What is " + firstNum + " " + op + " " + secondNum + "?";
    }
    
}
