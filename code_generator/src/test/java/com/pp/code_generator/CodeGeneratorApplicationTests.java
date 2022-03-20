package com.pp.code_generator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
class CodeGeneratorApplicationTests {

    @Test
    void contextLoads() throws IOException {
        String buffer = "";
        InputStream is = new FileInputStream("C:\\Users\\pengpan\\IdeaProjects\\back_general_system\\code_generator\\src\\main\\resources\\sqlsql\\sql.sql");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.concat(line); // 将读到的内容添加到 buffer 中
            buffer.concat("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        System.out.println(buffer);
    }

}
