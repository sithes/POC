package com.shopping.cart.app.model;

/**
 * @author Sithes
 *
 */
public class Product {
	private String productName;  
	private long quantity;
	
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @return the quantity
	 */
	public long getQuantity() {
		return quantity;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
   
}
