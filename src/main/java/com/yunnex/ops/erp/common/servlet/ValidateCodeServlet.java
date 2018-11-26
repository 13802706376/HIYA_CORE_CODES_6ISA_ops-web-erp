/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.common.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成随机验证码
 * @author ThinkGem
 * @version 2014-7-27
 */
@SuppressWarnings("serial")
public class ValidateCodeServlet extends HttpServlet {
    
    public static final String VALIDATE_CODE = "validateCode";
    
    private static final Logger logger=LoggerFactory.getLogger(ValidateCodeServlet.class);
    
    private static  final int wf = 70;
    private static  final int hf = 26;
    
    public static final int DEFAULT_NUMBER_255 = 255;
    public static final int DEFAULT_NUMBER_220 = 220;
    public static final int DEFAULT_NUMBER_250 = 250;
    public static final int DEFAULT_NUMBER_40 = 40;
    public static final int DEFAULT_NUMBER_8 = 8;
    public static final int DEFAULT_NUMBER_150 = 150;
    public static final int DEFAULT_NUMBER_4 = 4;
    public static final int DEFAULT_NUMBER_50 = 50;
    public static final int DEFAULT_NUMBER_100 = 100;
    public static final int DEFAULT_NUMBER_26 = 26;
    public static final int DEFAULT_NUMBER_15 = 15;
    public static final int DEFAULT_NUMBER_5 = 5;
    public static final int DEFAULT_NUMBER_19 = 19;
    
    private static int w = wf;
    private static int h = hf;
    
    public ValidateCodeServlet() {
        super();
    }
    
    public static boolean validate(HttpServletRequest request, String validateCode){
        String code = (String)request.getSession().getAttribute(VALIDATE_CODE);
        return validateCode.equalsIgnoreCase(code); 
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String validateCode = request.getParameter(VALIDATE_CODE); // AJAX验证，成功返回true
        if (StringUtils.isNotBlank(validateCode)){
            try {
                response.getOutputStream().print(validate(request, validateCode)?"true":"false");
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }else{
            try {
                this.doPost(request, response);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            createImage(request,response);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    
    private void createImage(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        
        /*
         * 得到参数高，宽，都为数字时，则使用设置高宽，否则使用默认值
         */
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
            w = NumberUtils.toInt(width);
            h = NumberUtils.toInt(height);
        }
        
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        /*
         * 生成背景
         */
        createBackground(g);

        /*
         * 生成字符
         */
        String s = createCharacter(g);
        request.getSession().setAttribute(VALIDATE_CODE, s);

        g.dispose();
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        out.close();

    }
    
    private static Color  getRandColor(int fc,int bc) { 
        int f = fc;
        int b = bc;
        SecureRandom random=new SecureRandom();
        if(f>DEFAULT_NUMBER_255) {
            f=DEFAULT_NUMBER_255; 
        }
        if(b>DEFAULT_NUMBER_255) {
            b=DEFAULT_NUMBER_255; 
        }
        return new Color(f+random.nextInt(b-f),f+random.nextInt(b-f),f+random.nextInt(b-f)); 
    }
    
    private void createBackground(Graphics g) {
        // 填充背景
        g.setColor(getRandColor(DEFAULT_NUMBER_220,DEFAULT_NUMBER_250)); 
        g.fillRect(0, 0, w, h);
        // 加入干扰线条
        for (int i = 0; i < DEFAULT_NUMBER_8; i++) {
            g.setColor(getRandColor(DEFAULT_NUMBER_40,DEFAULT_NUMBER_150));
            SecureRandom random = new SecureRandom();
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int x1 = random.nextInt(w);
            int y1 = random.nextInt(h);
            g.drawLine(x, y, x1, y1);
        }
    }

    private static String createCharacter(Graphics g) {
        char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
                'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
        String[] fontTypes = {"Arial","Arial Black","AvantGarde Bk BT","Calibri"}; 
        SecureRandom random = new SecureRandom();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < DEFAULT_NUMBER_4; i++) {
            String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
            g.setColor(new Color(DEFAULT_NUMBER_50 + random.nextInt(DEFAULT_NUMBER_100), DEFAULT_NUMBER_50 + random.nextInt(DEFAULT_NUMBER_100), DEFAULT_NUMBER_50 + random.nextInt(DEFAULT_NUMBER_100)));
            g.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)],Font.BOLD,DEFAULT_NUMBER_26)); 
            g.drawString(r, DEFAULT_NUMBER_15 * i + DEFAULT_NUMBER_5, DEFAULT_NUMBER_19 + random.nextInt(DEFAULT_NUMBER_8));
            s.append(r);
        }
        return s.toString();
    }
    

}
