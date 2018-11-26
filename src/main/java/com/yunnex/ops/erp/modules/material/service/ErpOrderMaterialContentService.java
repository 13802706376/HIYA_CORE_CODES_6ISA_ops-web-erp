package com.yunnex.ops.erp.modules.material.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.NumberUtils;
import com.yunnex.ops.erp.modules.material.constant.MaterialContentConstant;
import com.yunnex.ops.erp.modules.material.dao.ErpOrderMaterialContentDao;
import com.yunnex.ops.erp.modules.material.dao.ErpOrderMaterialCreationDao;
import com.yunnex.ops.erp.modules.material.dto.MaterialContentDto;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialContent;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.order.dto.OrderMaterialDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSendInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSendInfoService;

/**
 * 订单物料内容Service
 * 
 * @author yunnex
 * @version 2018-07-13
 */
@Service
public class ErpOrderMaterialContentService extends CrudService<ErpOrderMaterialContentDao, ErpOrderMaterialContent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpOrderMaterialContentService.class); // NOSONAR

    private static final String HEADER_MATERIAL_TYPE = "物料种类";
    private static final String HEADER_MATERIAL_QUALITY = "材质";
    private static final String HEADER_FRONT_IMAGE = "正面";
    private static final String HEADER_REVERSE_IMAGE = "反面";
    private static final String HEADER_SIZE = "尺寸";
    private static final String HEADER_MATERIAL_AMOUNT = "数量";
    private static final String ORDER_NUMBER = "订单号：";
    private static final String LINK_MAN = "联系人：";
    private static final String LINK_PHONE = "联系电话：";
    private static final String RECEIVE_ADDRESS = "收货地址：";
    private static final String REMIND = "特别提醒：“请认真核对物料内容及数量是否正确，再进行签收！”";
    private static final String FRONT_IMAGE = "正面图";
    private static final String REVERSE_IMAGE = "反面图";
    private static final String PDF_NAME = "物料清单.pdf";
    private static final int PDF_COLUMN = 6;
    private static final float PDF_WIDTH = 100F;
    private static final float SPACING_BEFORE = 30F;

    private static final String DASH = "- -";
    private static final String GUEST_RING_NAME = "拉客手环";

    private static final int ONE_KB = 1024;
    // PDF间距
    private static final float PDF_GAP = 20F;
    // PDF中文字段支持，使用itext-asian包（亚洲中日韩字体支持）方式.
    private static Font chineseFont;

    static {
        try {
            BaseFont chinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            chineseFont = new Font(chinese);
        } catch (Exception e) { // NOSONAR
            LOGGER.error("字体加载出错！", e);
        }
    }

    @Value("${domain.erp.res}")
    private String resDomain;

    @Autowired
    private ErpOrderSendInfoService orderSendInfoService;

    @Autowired
    private ErpOrderMaterialCreationDao orderMaterialCreationDao;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpOrderMaterialContent erpOrderMaterialContent) {
        super.save(erpOrderMaterialContent);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpOrderMaterialContent erpOrderMaterialContent) {
        super.delete(erpOrderMaterialContent);
    }

    public List<ErpOrderMaterialContent> findByOrderNumber(String orderNumber, String delFlag) {
        return dao.findByOrderNumber(orderNumber, delFlag);
    }

    public List<ErpOrderMaterialContent> findByYsOrderId(Long ysOrderId) {
        return dao.findByYsOrderId(ysOrderId);
    }

    @Transactional
    public int deleteByOrderNumber(String orderNumber) {
        return dao.deleteByOrderNumber(orderNumber);
    }

    @Transactional
    public int deleteByYsOrderId(Long ysOrderId) {
        return dao.deleteByYsOrderId(ysOrderId);
    }
    
    @Transactional
    public int saveBatch(List<ErpOrderMaterialContent> list) {
        return dao.saveBatch(list);
    }

    /**
     * 查询订单物料内容
     * 
     * @param orderNumber 贝虎订单号
     * @return
     */
    public BaseResult findOrderMaterials(String orderNumber) {
        logger.info("查询订单物料内容入参：orderNumber={}", orderNumber);
        if (StringUtils.isBlank(orderNumber)) {
            return new IllegalArgumentErrorResult("订单号不能为空！");
        }

        OrderMaterialDto orderMaterialDto = new OrderMaterialDto();
        orderMaterialDto.setOrderNumber(orderNumber);
        // 订单寄送信息
        ErpOrderSendInfo orderSendInfo = orderSendInfoService.findByOrderNumber(orderNumber);
        if (orderSendInfo != null) {
            orderMaterialDto.setOrderLinkMan(orderSendInfo.getLinkMan());
            orderMaterialDto.setOrderLinkPhone(orderSendInfo.getLinkPhone());
            orderMaterialDto.setOrderReceiveAddress(orderSendInfo.getReceiveAddress());
        }

        // 物料内容
        List<ErpOrderMaterialContent> materialContents = dao.findByOrderNumber(orderNumber, BaseEntity.DEL_FLAG_NORMAL);
        if (CollectionUtils.isNotEmpty(materialContents)) {
            List<MaterialContentDto> contentDtos = materialContents.stream().map(materialContent -> {
                MaterialContentDto materialContentDto = new MaterialContentDto();
                materialContentDto.setMaterialTypeName(materialContent.getMaterialTypeName());
                materialContentDto.setMaterialQuality(materialContent.getMaterialQuality());
                materialContentDto.setSize(materialContent.getSize());
                materialContentDto.setMaterialAmount(materialContent.getMaterialAmount());
                String frontName = materialContent.getFrontName();
                String reverseName = materialContent.getReverseName();
                materialContentDto.setFrontName(StringUtils.isNotBlank(frontName) ? frontName : DASH);
                materialContentDto.setReverseName(StringUtils.isNotBlank(reverseName) ? reverseName : DASH);
                String frontImage = materialContent.getFrontImage();
                String reverseImage = materialContent.getReverseImage();
                if (StringUtils.isNotBlank(frontImage)) {
                    materialContentDto.setFrontImage(frontImage);
                    materialContentDto.setFrontImageName(FilenameUtils.getBaseName(frontImage));
                }
                if (StringUtils.isNotBlank(reverseImage)) {
                    materialContentDto.setReverseImage(reverseImage);
                    materialContentDto.setReverseImageName(FilenameUtils.getBaseName(reverseImage));
                }
                // 拉客手环如果没有图片，显示一张默认图片
                if (MaterialContentConstant.GUEST_RING == materialContent.getScenarioType() && StringUtils.isBlank(frontImage) && StringUtils
                                .isBlank(reverseImage)) {
                    materialContentDto.setFrontImage(MaterialContentConstant.GUEST_RING_IMAGE);
                    materialContentDto.setFrontImageName(GUEST_RING_NAME);
                }
                return materialContentDto;
            }).collect(Collectors.toList());
            orderMaterialDto.setMaterialContents(contentDtos);
        }

        // 商户名称
        ErpOrderMaterialCreation orderMaterialCreation = orderMaterialCreationDao.findByOrderNumber(orderNumber);
        if (orderMaterialCreation != null) {
            orderMaterialDto.setShopName(orderMaterialCreation.getShopName());
        }

        return new BaseResult(orderMaterialDto);
    }

    /**
     * 生成PDF到内存中
     * 
     * @param orderMaterialDto
     * @param outputStream
     */
    public void genMaterialContentPdf(OrderMaterialDto orderMaterialDto, OutputStream outputStream) { // NOSONAR
        Document document = null;
        PdfWriter pdfWriter = null;
        try {
            document = new Document();
            pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();

            // 订单号
            String orderNo = orderMaterialDto.getOrderNumber();
            Paragraph orderNoPara = new Paragraph(ORDER_NUMBER + (orderNo != null ? orderNo : Constant.BLANK), chineseFont);
            orderNoPara.setSpacingAfter(PDF_GAP);
            document.add(orderNoPara);
            LineSeparator lineSeparator1 = new LineSeparator();
            document.add(lineSeparator1);
            String shopName = orderMaterialDto.getShopName();
            Paragraph shopNamePara = new Paragraph(shopName != null ? shopName : Constant.BLANK, chineseFont);
            shopNamePara.setAlignment(Element.ALIGN_CENTER);
            document.add(shopNamePara);

            List<MaterialContentDto> materialContents = orderMaterialDto.getMaterialContents();
            PdfPTable materialPTable = new PdfPTable(PDF_COLUMN);
            materialPTable.setSpacingBefore(PDF_GAP);
            materialPTable.setWidthPercentage(PDF_WIDTH);
            materialPTable.addCell(new Paragraph(HEADER_MATERIAL_TYPE, chineseFont));
            materialPTable.addCell(new Paragraph(HEADER_MATERIAL_QUALITY, chineseFont));
            materialPTable.addCell(new Paragraph(HEADER_FRONT_IMAGE, chineseFont));
            materialPTable.addCell(new Paragraph(HEADER_REVERSE_IMAGE, chineseFont));
            materialPTable.addCell(new Paragraph(HEADER_SIZE, chineseFont));
            materialPTable.addCell(new Paragraph(HEADER_MATERIAL_AMOUNT, chineseFont));
            if (CollectionUtils.isNotEmpty(materialContents)) {
                for (MaterialContentDto materialContentDto : materialContents) {
                    Paragraph materialTypePara = new Paragraph(materialContentDto.getMaterialTypeName(), chineseFont);
                    materialPTable.addCell(materialTypePara);
                    String materialQuality = materialContentDto.getMaterialQuality();
                    Paragraph materialQualityPara = new Paragraph(materialQuality, chineseFont);
                    materialPTable.addCell(materialQualityPara);
                    String frontImage = materialContentDto.getFrontImage();
                    if (StringUtils.isBlank(frontImage)) {
                        materialPTable.addCell(Constant.BLANK);
                    } else {
                        materialPTable.addCell(imageCell(frontImage));
                    }
                    String reverseImage = materialContentDto.getReverseImage();
                    if (StringUtils.isBlank(reverseImage)) {
                        materialPTable.addCell(Constant.BLANK);
                    } else {
                        materialPTable.addCell(imageCell(reverseImage));
                    }
                    String size = materialContentDto.getSize();
                    Paragraph sizePara = new Paragraph(size, chineseFont);
                    materialPTable.addCell(sizePara);
                    Integer materialAmount = materialContentDto.getMaterialAmount();
                    materialPTable.addCell(NumberUtils.nullToZero(materialAmount).toString());
                }
            }
            document.add(materialPTable);

            String linkMan = orderMaterialDto.getOrderLinkMan();
            Paragraph linkManPara = new Paragraph(LINK_MAN + (linkMan != null ? linkMan : Constant.BLANK), chineseFont);
            linkManPara.setSpacingBefore(SPACING_BEFORE);
            document.add(linkManPara);
            String linkPhone = orderMaterialDto.getOrderLinkPhone();
            Paragraph linkPhonePara = new Paragraph(LINK_PHONE + (linkPhone != null ? linkPhone : Constant.BLANK), chineseFont);
            document.add(linkPhonePara);
            String receiveAddress = orderMaterialDto.getOrderReceiveAddress();
            Paragraph receiveAddressPara = new Paragraph(RECEIVE_ADDRESS + (receiveAddress != null ? receiveAddress : Constant.BLANK), chineseFont);
            document.add(receiveAddressPara);
            Paragraph remindPara = new Paragraph(REMIND, chineseFont);
            remindPara.setSpacingAfter(PDF_GAP);
            document.add(remindPara);
        } catch (Exception e) { // NOSONAR
            String msg = "导出PDF出错！";
            logger.error(msg, e);
            throw new ServiceException(msg);
        } finally {
            if (document != null) {
                document.close();
            }
            if (pdfWriter != null) {
                pdfWriter.close();
            }
        }
    }

    /**
     * PDF下载图片，忽略异常
     * 
     * @param imageUrl
     * @return
     */
    private Image imageCell(String imageUrl) {
        try {
            return Image.getInstance(new URL(imageUrl));
        } catch (Exception e) { // NOSONAR
            logger.info("PDF图片下载失败！");
        }
        return null;
    }

    /**
     * 压缩物料内容
     * 
     * @param orderNumber
     * @return
     */
    public BaseResult compressOrderMaterials(String orderNumber, OutputStream outputStream) {
        BaseResult baseResult = findOrderMaterials(orderNumber);
        if (!BaseResult.isSuccess(baseResult)) {
            return baseResult;
        }

        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream, Charset.forName(Constant.CHARSET_UTF8));
                        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
            OrderMaterialDto orderMaterialDto = (OrderMaterialDto) baseResult.getAttach();

            // 生成并压缩PDF
            genMaterialContentPdf(orderMaterialDto, pdfOut);
            try (ByteArrayInputStream pdfIn = new ByteArrayInputStream(pdfOut.toByteArray())) {
                compress(zipOut, pdfIn, PDF_NAME);
            }

            // 下载并压缩物料图片
            compressMaterialImages(orderMaterialDto, zipOut);
        } catch (Exception e) { // NOSONAR
            String msg = "压缩失败！";
            logger.error(msg, e);
            return new SystemErrorResult(msg);
        }

        return new BaseResult();
    }

    /**
     * 下载并压缩物料图片
     * 
     * @param orderMaterialDto
     * @param zipOut
     */
    private void compressMaterialImages(OrderMaterialDto orderMaterialDto, ZipOutputStream zipOut) {
        if (orderMaterialDto == null || CollectionUtils.isEmpty(orderMaterialDto.getMaterialContents())) {
            return;
        }
        List<MaterialContentDto> materialContents = orderMaterialDto.getMaterialContents();
        int count;
        for (int i = 0; i < materialContents.size(); i++) {
            MaterialContentDto materialContentDto = materialContents.get(i);
            count = i + Constant.ONE;
            String frontImage = materialContentDto.getFrontImage();
            compressImage(zipOut, frontImage, FRONT_IMAGE + count + Constant.POINT + FilenameUtils.getExtension(frontImage));
            String reverseImage = materialContentDto.getReverseImage();
            compressImage(zipOut, reverseImage, REVERSE_IMAGE + count + Constant.POINT + FilenameUtils.getExtension(reverseImage));
        }
    }

    /**
     * 下载并压缩图片
     * 
     * @param fileName
     * @return
     */
    private void compressImage(ZipOutputStream zos, String fileName, String displayName) {
        if (StringUtils.isBlank(fileName)) {
            return;
        }
        try (InputStream inputStream = new URL(fileName).openConnection().getInputStream()) {
            compress(zos, inputStream, displayName);
        } catch (IOException e) {
            logger.error("图片压缩失败！{}", fileName, e);
        }
    }

    /**
     * zip压缩
     * 
     * @param zos
     * @param inputStream
     * @param displayName
     */
    public void compress(ZipOutputStream zos, InputStream inputStream, String displayName) {
        try {
            ZipEntry zipEntry = new ZipEntry(displayName);
            zos.putNextEntry(zipEntry);
            byte[] buff = new byte[ONE_KB];
            int len = 0;
            while ((len = inputStream.read(buff)) != -1) {
                zos.write(buff, Constant.ZERO, len);
            }
            zos.closeEntry();
        } catch (IOException e) {
            logger.error("压缩失败！{}", displayName, e);
        }
    }

}
