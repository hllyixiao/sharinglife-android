package com.sqin.libbaseframe.utils;

import android.app.Activity;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;


/**
 * 小抄，可以把写下对应题号的答案，并传给其他人用<br />
 * 好吧，其实是为了配合EventBus传递多个参数用的。<br />
 * 使用方法： <br />
 * 传递-eventBus.postSticky(new CheatSheet("接收暗号", 参数1, 参数2 ...))<br />
 * 接收参数参考以下方法：<br />
 * {@link CheatSheet#get()} <br />
 * {@link CheatSheet#get(Object key)} <br />
 * {@link CheatSheet#getWith(AnswerSheet)} <br />
 * {@link CheatSheet#getAnswer(String)} <br />
 * {@link CheatSheet#getAnswers(Object...)} <br />
 * 另外本类定义为抽象是因为若不为抽象，使用eventBus连续传递两个CheatSheet对象的话后一个会把前一个覆盖掉<br />
 * 作者：dsq
 * 日期：2016/3/10.
 */
public class CheatSheet implements Serializable{
    private static final String TAG = "CheatSheet";
    public static final String CODE_INTENT = "CodeIntent";

    private HashMap<Object, Object> map = new HashMap<>();

    /**
     * 暗号，检验暗号查看是不是给你的
     */
    public String code;

    /**
     * 当前默认题号
     */
    int currentQuestionNo = 0;

    /**
     * 当前写入的默认题号
     */
    int currentWriteNo = 0;

    public CheatSheet(){
    }

    /**
     * 撕出一张纸做小抄，并写上暗号<br />
     *
     * @param code 暗号
     */
    public CheatSheet(String code){
        this.code = code;
    }

    /**
     * 撕出一张纸做小抄，写上暗号，并按题号顺序写入多个题的答案 (ACBCA CACAD)<br />
     * 可以通过{@link CheatSheet#get()} 来按输入顺序获得答案<br />
     * 也可以通过{@link CheatSheet#getWith(AnswerSheet)} 来向AnswerSheet对象的标记了{@link com.pmavio.pmbaseframe.utils.CheatSheet.HardQuestion}的参数按输入顺序写入答案<br />
     * @param code 暗号
     * @param answers 多个题的答案
     */
    public CheatSheet(String code, Object... answers){
        this(code);
        if(answers != null && answers.length > 0){
            for(currentQuestionNo = 0 ; currentQuestionNo < answers.length ; currentQuestionNo++){
                put(currentQuestionNo, answers[currentQuestionNo]);
            }
        }
    }

    /**
     * 返回当前默认题号
     * @return
     */
    public int getCurrentQuestionNo(){
        return currentQuestionNo;
    }

    public boolean checkCode(String code){
        if(this.code == null){
            return code == this.code;
        }
        return this.code.endsWith(code);
    }

    /**
     * 按题号顺序写下一个答案
     * @param answer
     * @return 当前写入的答案
     */
    public Object put(Object answer){
        currentQuestionNo++;
        return put(currentQuestionNo, answer);
    }

    /**
     * 写下一道题的题号和答案 (19.x=0)
     * @param question 题号
     * @param answer 答案
     * @return 当前写入的答案
     */
    public Object put(Object question, Object answer) {
        return map.put(question, answer);
    }

    /**
     * 按顺序找下一道题的答案
     * @param <T> 答案的类型
     * @return 答案
     */
    public <T> T get(){
        return get(currentWriteNo ++);
    }

    public <T> T get(Object key){
        return (T) map.get(key);
    }

    /**
     * 找到一道题的答案
     * @param question 题号
     * @return 答案
     */
    public <T> T getAnswer(String question) {
        return (T) map.get(question);
    }

    /**
     * 按题号顺序记下答案 (ABBCA CAACD)
     * @param questions 答题卡
     */
    public void getAnswers(Object... questions){
        if(questions == null || questions.length <= 0 ) return;
        for(int i=0; i<= currentQuestionNo; i++){
            questions[i] = get(i);
        }
    }

    /**
     * 直接把答案抄到答题卡上
     * @param sheet 答题卡
     */
    public void getWith(AnswerSheet sheet){
        if(sheet == null) return; //根本没有答题卡
        Field[] questions = sheet.getClass().getDeclaredFields();
        if(questions == null || questions.length <= 0) return;
        for(Field question : questions){
            question.setAccessible(true);
            HardQuestion q = question.getAnnotation(HardQuestion.class);
            if(q == null) continue;

            String[] codes = q.code();
            if(code != null && codes != null && codes.length > 0){ //对比暗号，找找有没有给你的，如果一个暗号都没有，那就直接抄
                boolean isForMe = false;
                for(String code : codes){
                    if(this.code.equals(code)){
                        isForMe = true;
                        break;
                    }
                }
                if(!isForMe) continue;//没有一个暗号能对上，不是给你的小抄
            }

                Object answer = null;
                if(!q.question().equals("")) {
                    answer = get(q.question());
                }else if(q.questionNo() >= 0){
                    answer = get(q.questionNo());
                }else{
                answer = get(currentWriteNo);
                currentWriteNo ++;
            }
            if(answer == null) continue;
            try {

                Class aClass = answer.getClass();
                Class qClass = question.getType();
                if(String.class.isAssignableFrom(aClass)){
                    //把string类型的answer处理成question类型
                    String value = (String) answer;
                    if(Boolean.class.isAssignableFrom(qClass) || boolean.class.isAssignableFrom(qClass)){
                        answer = Boolean.parseBoolean(value);
                    }else if(Integer.class.isAssignableFrom(qClass) || int.class.isAssignableFrom(qClass)){
                        answer = Integer.parseInt(value);
                    }else if(Float.class.isAssignableFrom(qClass) || float.class.isAssignableFrom(qClass)){
                        answer = Float.parseFloat(value);
                    }else if(Long.class.isAssignableFrom(qClass) || long.class.isAssignableFrom(qClass)) {
                        answer = Long.parseLong(value);
                    }else if(Double.class.isAssignableFrom(qClass) || double.class.isAssignableFrom(qClass)) {
                        answer = Double.parseDouble(value);
                    }
                }else if(String.class.isAssignableFrom(qClass)){
                    //如果question类型为String，而answer类型非String，则把answer转换成String
                    answer = answer.toString();
                }

                question.set(sheet, answer); // 抄答案...
            } catch (Exception e) {
                Log.e(TAG, "答案(" + answer + ")似乎和题目(" + question.getName() + ")不匹配", e);
            }
        }
    }

    public static void getAnswerByActivity(Activity act){
        if(act == null) return;
        if(!(act instanceof AnswerSheet)) return;
        Intent intent = act.getIntent();
        if(intent == null || !intent.hasExtra(CheatSheet.CODE_INTENT)) return;
        try{
            CheatSheet cheatSheet = (CheatSheet) intent.getSerializableExtra(CheatSheet.CODE_INTENT);
            AnswerSheet answerSheet = (AnswerSheet) act;
            cheatSheet.getWith(answerSheet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Intent putExtra(Intent intent, Object... extras){
        if(intent == null) intent = new Intent();
        CheatSheet cheatSheet = new CheatSheet(null, extras);
        intent.putExtra(CODE_INTENT, cheatSheet);
        return intent;
    }

    public static CheatSheet getFromIntent(Intent intent){
        if(intent == null) return null;
        CheatSheet cheatSheet = (CheatSheet) intent.getSerializableExtra(CODE_INTENT);
        return cheatSheet;
    }

    /**
     * 回收本小抄，别再继续传了，不然要被监考发现了<br />
     * 在使用{@link EventBus#postSticky(Object)} 时，如果不回收则本小抄会一直存在
     * @return 回收成功状态
     */
    public boolean recycle(){
        return EventBus.getDefault().removeStickyEvent(this);
    }

    /**
     * 回收本小抄，别再继续传了，不然要被监考发现了<br />
     * 在使用{@link EventBus#postSticky(Object)} 时，如果不回收则本小抄会一直存在
     * @return 回收成功状态
     */
    public boolean recycle(String code){
        if(checkCode(code))
            return EventBus.getDefault().removeStickyEvent(this);
        else
            return false;
    }

    /**
     * 答题卡
     * 实现了答题卡的类可以直接调用{@link CheatSheet#getWith(AnswerSheet)}来填满标记了@{@link com.pmavio.pmbaseframe.utils.CheatSheet.HardQuestion}答案
     */
    public static interface AnswerSheet {}

    /**
     * 难题标记，用于标记在答题卡中不会做的题的题号上，就可以在拿到小抄之后直接抄到答题卡上
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface HardQuestion {

        /**
         * 题号，在小抄中优先找对应的题号
         * @return
         */
        String question() default "";

        /**
         * 默认题号，没有写题号时按默认题号取，默认题号从0开始
         * @return
         */
        int questionNo();

        /**
         * 暗号，可以设置多个暗号，方便四面八方的兄弟一起支援
         * @return
         */
        String[] code() default {};
    }
}
