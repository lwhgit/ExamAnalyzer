import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Analyzer {
    private String fileName;
    private ArrayList<ExamData> examDataList;

    private String content;
    private ArrayList<Float[]> percentList;

    public Analyzer(String fileName) {
        this.fileName = fileName;
        examDataList = new ArrayList<>();
        percentList = new ArrayList<>();
    }

    public void parseFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                examDataList.add(new ExamData(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void analyze() {
        ArrayList<Integer[]> answerCountList = new ArrayList<>();

        for (int i = 0;i < examDataList.size();i ++) {
            ExamData data = examDataList.get(i);
            Integer[] count;

            for (int j = 0;j < 20;j ++) {

                if (i > 0) {
                    count = answerCountList.get(j);
                } else {
                    count = new Integer[] {
                            0, 0, 0, 0, 0
                    };
                }

                count[data.getAnswerOfProblem(j) - 1] ++;

                if (i > 0) {
                    answerCountList.set(j, count);
                } else {
                    answerCountList.add(count);
                }
            }
        }

        content = "";

        content += "분석 대상\n";
        for (int i = 0;i < examDataList.size();i ++) {
            content += "    " + examDataList.get(i).getTag() + "\n";
        }

        content += "\n";
        content += "            │                        갯수                      \n";
        content += "Num of Plob │        1번        2번        3번        4번        5번\n";
        for (int i = 0;i < answerCountList.size();i ++) {
            Integer[] count = answerCountList.get(i);
            content += String.format("%10d. │", (i+1));
            Float[] percent = new Float[5];
            for (int j = 0;j < 5;j ++) {
                content += String.format("%10d", count[j]);

                percent[j] = ((float) count[j]) / ((float) examDataList.size()) * 100;
            }
            percentList.add(percent);
            content += "\n";
        }

        content +="\n";
        content += "            │                        Percent                   \n";
        content += "Num of Plob │        1번        2번        3번        4번        5번\n";
        for (int i = 0;i < answerCountList.size();i ++) {
            content += String.format("%10d. │", (i+1));
            for (int j = 0;j < 5;j ++) {
                content += String.format("%10.3f", percentList.get(i)[j]);
            }
            content += "\n";
        }

        content +="\n";
        content += "            │                                                  \n";
        content += "Recmded Ans │       1순위       2순위        3순위       4순위       5순위\n";
        for (int i = 0;i < percentList.size();i ++) {
            content += String.format("%10d. │", (i+1));
            Float[] percent = percentList.get(i);
            content += func(percent);
            content += "\n";
        }

        System.out.println("Complete!");
        writeFile(fileName + "out", content);
    }

    private String func(Float[] percent) {
        Integer[] number = new Integer[] {1, 2, 3, 4, 5};
        String str = "";

        for (int i = 0;i < 5;i ++ ) {
            for (int j = i;j < 5;j ++) {
                if (percent[i] < percent[j]) {
                    percent[i] += percent[j];
                    percent[j] = percent[i] - percent[j];
                    percent[i] = percent[i] - percent[j];
                    number[i] += number[j];
                    number[j] = number[i] - number[j];
                    number[i] = number[i] - number[j];
                }
            }
        }

        str += String.format("%10d",  number[0]);
        for (int i = 1;i < 5;i ++) {
            str += "    " + (Math.abs(percent[i - 1] - percent[i]) < 0.1f ? "=" : ">");
            str += String.format("%5d", number[i]);
        }

        /*
        Arrays.sort(percent);
        for (int i = 0;i < percent.length;i ++) {
            percent[i] += 100 * (i + 1);
            System.out.println(percent[i]);
        }
        str += String.format("%10d",  ((int) (percent[4] / 100)));
        for (int i = percent.length - 2;i >= 0;i --) {
            float real1 = percent[i + 1] % 100;
            float real2 = percent[i] % 100;
            System.out.println(real1 + "  " + real2);
            str += "    " + (real1 == real2 ? "=" : ">");
            str += String.format("%4d",  ((int) (percent[i] / 100)));
        }*/
        return str;
    }

    public void writeFile(String path, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ExamData {
        private String tag;
        private ArrayList<Integer> answerList;

        public ExamData(String str) throws Exception {
            String[] tmpToken = str.split("[|]");
            String[] answerToken = tmpToken[1].split(" ");

            tag = tmpToken[0];
            answerList = new ArrayList<>();

            for (String s : answerToken) {
                int i = Integer.parseInt(s);
                if (i > 5 || i < 1) {
                    System.out.println("잘못된 정답이 있습니다.");
                    throw new Exception();
                }
                answerList.add(i);
            }
        }

        public String getTag() {
            return tag;
        }

        public int getAnswerOfProblem(int number) {
            return answerList.get(number);
        }
    }
}
