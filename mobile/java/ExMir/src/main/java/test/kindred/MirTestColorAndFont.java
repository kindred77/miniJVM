package test.kindred;

import org.mini.gl.GL;

import static org.mini.gl.GL.*;
import static org.mini.glfw.Glfw.*;

import static org.mini.nanovg.Nanovg.stbtt_GetCodepointBitmapBox;
import static org.mini.nanovg.Nanovg.stbtt_GetCodepointHMetrics;
import static org.mini.nanovg.Nanovg.stbtt_GetCodepointKernAdvance;
import static org.mini.nanovg.Nanovg.stbtt_GetFontVMetrics;
import static org.mini.nanovg.Nanovg.stbtt_InitFont;
import static org.mini.nanovg.Nanovg.stbtt_MakeCodepointBitmapOffset;
import static org.mini.nanovg.Nanovg.stbtt_MakeFontInfo;
import static org.mini.nanovg.Nanovg.stbtt_ScaleForPixelHeight;

import test.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gust
 */
public class MirTestColorAndFont {

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
            + "in vec2 TexCoord; \n"
            + "\n"
            + "out vec4 FragColor; \n"
            + "\n"
            + "uniform sampler2D textureSampler; \n"
            + "\n"
            + "uniform vec4 colorTint; // 用于调整颜色的向量 \n"
            + "\n"
            + "void main(){ \n"
            + "vec4 originalColor = texture(textureSampler, TexCoord); \n"
            + "FragColor = originalColor * colorTint; \n"
            + "} \n";

    String s_v2 = "#version 330 \n"
            + "layout (location = 0) in vec3 aPos; \n"
            + "layout (location = 1) in vec2 aTexCoord; \n"
            + "\n"
            + "out vec2 TexCoord;\n"
            + "\n"
            + "void main(){ \n"
            + "gl_Position = vec4(aPos, 1.0); \n"
            + "TexCoord = aTexCoord; \n"
            + "} \n";
    
    String s_f2 = "#version 330 \n"
            + "in vec2 TexCoord; \n"
            + "\n"
            + "out vec4 color; \n"
            + "\n"
            + "uniform sampler2D text; \n"
            + "\n"
            + "uniform vec3 textColor; \n"
            + "\n"
            + "void main(){ \n"
            + "vec4 sampled = vec4(0.0, 1.0, 1.0, texture(text, TexCoord).r); \n"
            //+ "color = vec4(textColor, 1.0) * sampled; \n"
            + "color = sampled; \n"
            + "} \n";

    int[] VAOs = {0,0};
    int[] BOs = {0,0};

    int[] rendertarget = {0};
    int[] fontTexture = {0};

    //int brightnessLoc = 0;

    int program;
    int program2;

    void createFontTexture()
    {
        byte[] fontBuffer = GToolkit.readFileFromFile("C:/Windows/Fonts/simkai.ttf");
        //byte[] fontBuffer = GToolkit.readFileFromFile("NotoEmoji+NotoSansCJKSC-Regular.TTF");
        if (fontBuffer != null)
        {
            System.out.println("ttf file size: " + fontBuffer.length);
        }
        /* prepare font */
        byte[] info = stbtt_MakeFontInfo();
        long infoPtr = GToolkit.getArrayDataPtr(info);
        if (stbtt_InitFont(infoPtr, fontBuffer, 0) == 0) {
            System.out.println("failed\n");
        }
        int b_w = 1400;
        /* bitmap width */
        int b_h = 66;
        /* bitmap height */
        int l_h = 64;
        /* line height */
        /* create a bitmap for the phrase */
        byte[] bitmap = new byte[b_w * b_h];
        //long bitmapPtr = GToolkit.getArrayDataPtr(bitmap);
        /* calculate font scaling */
        float scale = stbtt_ScaleForPixelHeight(infoPtr, l_h);
        String word = "hello，my name is 唐晔 ~!@#$%^&*()_+{}|:\"?><";
        int x = 0;
        System.out.println("---000---\n");
        int[] ascent = {0}, descent = {0}, lineGap = {0};
        stbtt_GetFontVMetrics(infoPtr, ascent, descent, lineGap);
        ascent[0] *= scale;
        descent[0] *= scale;

        int i;
        for (i = 0; i < word.length(); ++i) {
            int ch = word.charAt(i);//word[i];
            int nch = i < word.length() - 1 ? word.charAt(i + 1) : 0;//word[i + 1];
            /* get bounding box for character (may be offset to account for chars that dip above or below the line */
            int[] c_x1 = {0}, c_y1 = {0}, c_x2 = {0}, c_y2 = {0};
            stbtt_GetCodepointBitmapBox(infoPtr, ch, scale, scale, c_x1, c_y1, c_x2, c_y2);
            /* compute y (different characters have different heights */
            int y = ascent[0] + c_y1[0];

            /* render character (stride and offset is important here) */
            int byteOffset = x + (y * b_w);
            //System.out.println("---111---\n");
            stbtt_MakeCodepointBitmapOffset(infoPtr, bitmap, byteOffset, c_x2[0] - c_x1[0], c_y2[0] - c_y1[0], b_w, scale, scale, ch);
            //stbtt_MakeCodepointBitmapOffset(infoPtr, bitmap, 20, 10, 10, 2560, 0.05f, 0.05f, ch);
            /* how wide is this character */
            int[] ax = {0}, bx = {0};
           // System.out.println("---222---\n");
            stbtt_GetCodepointHMetrics(infoPtr, ch, ax, bx);
            x += ax[0] * scale;
            /* add kerning */
            //System.out.println("---333---\n");
            int kern = stbtt_GetCodepointKernAdvance(infoPtr, ch, nch);
            x += kern * scale;
        }

        glGenTextures(1, fontTexture, 0);
        glBindTexture(GL_TEXTURE_2D, fontTexture[0]);
        //设置顶点
        // float vertices[] = {
        //     // positions          // colors           // texture coords
        //     1.0f,  1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
        //     1.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   1.0f, 0.0f, // bottom right
        //     -1.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   0.0f, 0.0f, // bottom left
        //     -1.0f,  1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   0.0f, 1.0f  // top left 
        // };
        float vertices[] = {
            // positions          // colors           // texture coords
            1.0f,  0.5f, 0.0f,   0.0f, 0.0f, 0.0f,   1.0f, 0.0f, // top right
            1.0f, -0.5f, 0.0f,   0.0f, 0.0f, 0.0f,   1.0f, 1.0f, // bottom right
            -1.0f, -0.5f, 0.0f,   0.0f, 0.0f, 0.0f,   0.0f, 1.0f, // bottom left
            -1.0f,  0.5f, 0.0f,   0.0f, 0.0f, 0.0f,   0.0f, 0.0f  // top left 
        };
        glGenVertexArrays(1, VAOs, 1);
        glGenBuffers(1, BOs, 1);
        glBindVertexArray(VAOs[1]);
        glBindBuffer(GL_ARRAY_BUFFER, BOs[1]);
        // glBufferData(GL_ARRAY_BUFFER, (long) (vertices.length * 4), vertices, 0, GL_STATIC_DRAW);
        // glEnableVertexAttribArray(0);
        // glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 5 * 4, null, 0);
        // glEnableVertexAttribArray(1);
        // glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, 5 * 4, null, 3 * 4);
        glBufferData(GL_ARRAY_BUFFER, (long) (vertices.length * 4), vertices, 0, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * 4, null, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 8 * 4, null, 3 * 4);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, 8 * 4, null, 6 * 4);
        glEnableVertexAttribArray(2);
        
        //设置纹理环绕方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        System.out.println("---444---\n");
        //设置纹理过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        System.out.println("---555---\n");
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, b_w, b_h, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap, 0);
        glGenerateMipmap(GL_TEXTURE_2D);
        System.out.println("---666---\n");

        int vertex_shader, fragment_shader;
        vertex_shader = loadShader(GL_VERTEX_SHADER, s_v);
        fragment_shader = loadShader(GL_FRAGMENT_SHADER, s_f2);
        program2 = linkProgram(vertex_shader, fragment_shader);
    }

    void init() {
        float vertices[] = {
            // positions          // colors           // texture coords
            1.0f,  1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   1.0f, 1.0f, // top right
            1.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   1.0f, 0.0f, // bottom right
            -1.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   0.0f, 0.0f, // bottom left
            -1.0f,  1.0f, 0.0f,   0.0f, 0.0f, 0.0f,   0.0f, 1.0f  // top left 
        };
        glGenVertexArrays(1, VAOs, 0);
        glGenBuffers(1, BOs, 0);
        glBindVertexArray(VAOs[0]);
        glBindBuffer(GL_ARRAY_BUFFER, BOs[0]);
        glBufferData(GL_ARRAY_BUFFER, (long) (vertices.length * 4), vertices, 0, GL_STATIC_DRAW);
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
        byte[] d = GToolkit.image_parse_from_file_path("C:/mywork/projects/java/miniJVM/mobile/java/ExMir/src/main/resource/res/fern.png", whd);
        //byte[] d = GToolkit.image_parse_from_file_path("C:\\mywork\\projects\\java\\miniJVM\\binary\\win_x64/out.png", whd);
        
        if (d != null)
        {
            int format = whd[2] < 4 ? GL_RGB : GL_RGBA;
            System.out.println("Load png, format: "+format+", whd[0]: "+whd[0]+", whd[1]: "+whd[0]);
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

        createFontTexture();

        //brightnessLoc = glGetUniformLocation(program, GToolkit.toCstyleBytes("brightness"));
        
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

        glEnable(GL_BLEND); // 启用混合
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // 设置源因子和目标因子

        glActiveTexture(GL_TEXTURE0);

        //渲染图片
        glBindTexture(GL_TEXTURE_2D, rendertarget[0]);
        glUseProgram(program);
        //glUniform1f(brightnessLoc, 1.5f);
        // 设置纹理采样器的位置
        glUniform1i(glGetUniformLocation(program, GToolkit.toCstyleBytes("textureSampler")), 0);
        //设置颜色处理
        glUniform4f(glGetUniformLocation(program, GToolkit.toCstyleBytes("colorTint")), 1.0f, 0.5f, 0.5f, 1.0f);

        glBindVertexArray(VAOs[0]);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);

        //开始渲染字体
        glBindTexture(GL_TEXTURE_2D, fontTexture[0]);

        glUseProgram(program2);
        //glUniform1f(brightnessLoc, 1.5f);
        // 设置纹理采样器的位置
        glUniform1i(glGetUniformLocation(program, GToolkit.toCstyleBytes("text")), 0);
        //设置颜色处理
        glUniform3f(glGetUniformLocation(program, GToolkit.toCstyleBytes("textColor")), 1.0f, 1.0f, 1.0f);

        glBindVertexArray(VAOs[1]);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);

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
        MirTestColorAndFont gt = new MirTestColorAndFont();
        gt.t1();

    }
}
