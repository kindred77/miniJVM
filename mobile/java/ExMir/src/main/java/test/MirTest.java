package test;

import org.mini.gl.GL;

import static org.mini.gl.GL.*;
import static org.mini.glfw.Glfw.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gust
 */
public class MirTest {

    boolean exit = false;
    long curWin;
    int mx, my;

    class CallBack extends GlfwCallbackAdapter {

        @Override
        public void key(long window, int key, int scancode, int action, int mods) {
            System.out.println("key:" + key + " action:" + action);
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, GLFW_TRUE);
            }
        }

        @Override
        public void mouseButton(long window, int button, boolean pressed) {
            if (window == curWin) {
                String bt = button == GLFW_MOUSE_BUTTON_LEFT ? "LEFT" : button == GLFW_MOUSE_BUTTON_2 ? "RIGHT" : "OTHER";
                String press = pressed ? "pressed" : "released";
                System.out.println(bt + " " + mx + " " + my + "  " + press);
            }
        }

        @Override
        public void cursorPos(long window, int x, int y) {
            curWin = window;
            mx = x;
            my = y;
        }

        @Override
        public boolean windowClose(long window) {
            System.out.println("byebye");
            return true;
        }

        @Override
        public void windowSize(long window, int width, int height) {
            System.out.println("resize " + width + " " + height);
        }

        @Override
        public void framebufferSize(long window, int x, int y) {
        }
    }

    int loadShader(int shaderType, String shaderStr) {
        int[] return_val = {0};
        int fragment_shader = glCreateShader(shaderType);
        glShaderSource(fragment_shader, 1, new byte[][]{GToolkit.toCstyleBytes(shaderStr)}, null, 0);
        glCompileShader(fragment_shader);
        GL.glGetShaderiv(fragment_shader, GL.GL_COMPILE_STATUS, return_val, 0);
        if (return_val[0] == GL_FALSE) {
            GL.glGetShaderiv(fragment_shader, GL.GL_INFO_LOG_LENGTH, return_val, 0);
            byte[] szLog = new byte[return_val[0] + 1];
            GL.glGetShaderInfoLog(fragment_shader, szLog.length, return_val, 0, szLog);
            System.out.println("Compile Shader fail error :" + new String(szLog, 0, return_val[0]) + "\n" + shaderStr + "\n");
            return 0;
        }
        return fragment_shader;
    }

    int linkProgram(int vertexShader, int fragmentShader) {
        int[] return_val = {0};
        int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        GL.glGetProgramiv(program, GL.GL_LINK_STATUS, return_val, 0);
        if (return_val[0] == GL_FALSE) {
            GL.glGetProgramiv(program, GL.GL_INFO_LOG_LENGTH, return_val, 0);
            byte[] szLog = new byte[return_val[0] + 1];
            GL.glGetProgramInfoLog(program, szLog.length, return_val, 0, szLog);
            System.out.println("Link Shader fail error :" + new String(szLog, 0, return_val[0]) + "\n vertex shader:" + vertexShader + "\nfragment shader:" + fragmentShader + "\n");
            return 0;
        }
        return program;
    }

    String s_v = "#version 330 \n"
            + "layout (location = 0) in vec3 aPos; \n"
            + "layout (location = 1) in vec3 aColor; \n"
            + "layout (location = 2) in vec2 aTexCoord; \n"
            + "\n"
            + "out vec3 ourColor;\n"
            + "out vec2 TexCoord;\n"
            + "\n"
            + "void main(){ \n"
            + "gl_Position = vec4(aPos, 1.0); \n"
            + "ourColor = aColor; \n"
            + "TexCoord = vec2(aTexCoord.x, aTexCoord.y); \n"
            + "} \n";

    String s_f = "#version 330 \n"
            + "out vec4 FragColor; \n"
            + "\n"
            + "in vec3 ourColor; \n"
            + "in vec2 TexCoord; \n"
            + "uniform sampler2D texture1; \n"
            + "void main(){ \n"
            + "FragColor = texture(texture1, TexCoord); \n"
            + "} \n";

    //int vaoIndex = 0, vaoCount = 1;
    //int bufIndex = 0, bufCount = 1;
    //int eboIndex = 0, eboCount = 1;
    int vPosition = 0;

    int[] VAOs = {0};
    int[] BOs = {0};
    int[] EBOs = {0};

    int[] rendertarget = {0};
    //int VBO, VAO,EBO;

    int vecCount = 6;
        // 我们首先指定了要渲染的两个三角形的位置信息.  
        float[] vertices = new float[]{
            -0.90f, -0.90f, 0, // Triangle 1  
            0.85f, -0.90f, 0,
            -0.90f, 0.85f, 0,
            0.90f, -0.85f, 0, // Triangle 2  
            0.90f, 0.90f, 0,
            -0.85f, 0.90f, 0
        };

        int program;

    void init() {
        float vertices[] = {
            // positions          // colors           // texture coords
            0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, // bottom left
            -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f  // top left 
        };
        int indices[] = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
        };

        //glGenVertexArrays(1, &VAO);
        glGenVertexArrays(1, VAOs, 0);
        //glGenBuffers(1, &VBO);
        glGenBuffers(1, BOs, 0);
        //glGenBuffers(1, &EBO);
        glGenBuffers(1, EBOs, 0);

        glBindVertexArray(VAOs[0]);
        glBindBuffer(GL_ARRAY_BUFFER, BOs[0]);
        glBufferData(GL_ARRAY_BUFFER, (long) (vertices.length * 4), vertices, 0, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) (indices.length * 4), indices, 0, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * 4, null, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 8 * 4, null, 3 * 4);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, 8 * 4, null, 6 * 4);
        glEnableVertexAttribArray(2);



        //加入纹理
        //unsigned int texture;
        glGenTextures(1, rendertarget, 0);
        glBindTexture(GL_TEXTURE_2D, rendertarget[0]);
        //设置纹理环绕方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //设置纹理过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int[] whd = {0, 0, 0};
        //texture1[0] = gl_image_load(GToolkit.readFileFromJar("/res/fern.png"), whd);
        byte[] d = GToolkit.image_parse_from_file_path("C:/mywork/projects/java/miniJVM/mobile/java/ExMir/src/main/resource/res/pine.png", whd);
        if (d != null)
        {
            int format = whd[2] < 4 ? GL_RGB : GL_RGBA;
            System.out.println("Load png, format: "+format);
            glTexImage2D(GL_TEXTURE_2D, 0, format, whd[0], whd[1], 0, format, GL_UNSIGNED_BYTE, d, 0);
            //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, whd[0], whd[1], 0, GL_RGB, GL_UNSIGNED_BYTE, d);
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        else
        {
            System.out.println("Failed to load texture");
            return;
        }

        int vertex_shader, fragment_shader;
        vertex_shader = loadShader(GL_VERTEX_SHADER, s_v);
        fragment_shader = loadShader(GL_FRAGMENT_SHADER, s_f);
        program = linkProgram(vertex_shader, fragment_shader);

        //glUseProgram(program);
        // 最后这部分我们成为shader plumbing,  
        // 我们把需要的数据和shader程序中的变量关联在一起,后面会详细讲述  
        //glVertexAttribPointer(vPosition, 3, GL_FLOAT, GL_FALSE, 0, null, 0);
        //glEnableVertexAttribArray(vPosition);

    }


//---------------------------------------------------------------------  
//  
// display  
//  
// 这个函数是真正进行渲染的地方.它调用OpenGL的函数来请求数据进行渲染.  
// 几乎所有的display函数都会进行下面的三个步骤.  
//  
    void display(long win) {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);


        glBindTexture(GL_TEXTURE_2D, rendertarget[0]);

        glUseProgram(program);
        glBindVertexArray(VAOs[0]);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, null, 0);

        glfwSwapBuffers(win);

        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        }

    }

    void t1() {
        glfwInit();
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//        glfwWindowHint(GLFW_DEPTH_BITS, 16);
//        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        long win = glfwCreateWindow(640, 480, "hello glfw".getBytes(), 0, 0);
        if (win != 0) {
            glfwSetCallback(win, new CallBack());
            glfwMakeContextCurrent(win);
            //glfwSwapInterval(1);

            int w = glfwGetFramebufferWidth(win);
            int h = glfwGetFramebufferHeight(win);
            System.out.println("w=" + w + "  ,h=" + h);
            init();
            long last = System.currentTimeMillis(), now;
            int count = 0;
            while (!glfwWindowShouldClose(win)) {

                display(win);

                glfwPollEvents();
                glfwSwapBuffers(win);
                count++;
                now = System.currentTimeMillis();
                if (now - last > 1000) {
                    System.out.println("fps:" + count);
                    last = now;
                    count = 0;
                }
            }
            glfwTerminate();
        }
    }

    public static void main(String[] args) {
        MirTest gt = new MirTest();
        gt.t1();

    }
}
