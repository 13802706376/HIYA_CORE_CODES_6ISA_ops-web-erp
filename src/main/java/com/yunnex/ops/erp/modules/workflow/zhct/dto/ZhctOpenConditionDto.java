package com.yunnex.ops.erp.modules.workflow.zhct.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.zhct.entity.ErpZhctProductRecord;

/**
 * 智慧餐厅开通前置条件DTO
 * @author Administrator
 *
 */
public class ZhctOpenConditionDto {
	private String  isShopPrinter;//商户店内是否有打印机
	private String  printerNum;//打印机数量
	private String  printerInfoJson;//打印机信息
	private String  isConnectPrinter;//店内交换机接口是否足够连接打印机
	private String  isShopCashRegister;//商户店内是否有收银机
	private String  cashRegisterNum;//收银机数量
	private String  cashRegisterInfoJson;//收银机信息
	private String  isConnectInternet;//安装智慧餐厅客户端的电脑是否可以联网
	private String  shopDesc;//商户店内布线及桌台场景
	public String getIsShopPrinter() {
		return isShopPrinter;
	}
	public void setIsShopPrinter(String isShopPrinter) {
		this.isShopPrinter = isShopPrinter;
	}
	public String getPrinterNum() {
		return printerNum;
	}
	public void setPrinterNum(String printerNum) {
		this.printerNum = printerNum;
	}
	public String getPrinterInfoJson() {
		return printerInfoJson;
	}
	public void setPrinterInfoJson(String printerInfoJson) {
		this.printerInfoJson = printerInfoJson;
	}
	public String getIsConnectPrinter() {
		return isConnectPrinter;
	}
	public void setIsConnectPrinter(String isConnectPrinter) {
		this.isConnectPrinter = isConnectPrinter;
	}
	public String getIsShopCashRegister() {
		return isShopCashRegister;
	}
	public void setIsShopCashRegister(String isShopCashRegister) {
		this.isShopCashRegister = isShopCashRegister;
	}
	public String getCashRegisterNum() {
		return cashRegisterNum;
	}
	public void setCashRegisterNum(String cashRegisterNum) {
		this.cashRegisterNum = cashRegisterNum;
	}
	public String getCashRegisterInfoJson() {
		return cashRegisterInfoJson;
	}
	public void setCashRegisterInfoJson(String cashRegisterInfoJson) {
		this.cashRegisterInfoJson = cashRegisterInfoJson;
	}
	public String getIsConnectInternet() {
		return isConnectInternet;
	}
	public void setIsConnectInternet(String isConnectInternet) {
		this.isConnectInternet = isConnectInternet;
	}
	public String getShopDesc() {
		return shopDesc;
	}
	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}

}
