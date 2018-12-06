package Part3.Chapter11.Chapter11_11_4.App;

/*
 * 11.4.1 할인 서비스 구현
 * 
 * 최저가격 검색 애플리케이션은 여러 상점에서 가격 정보를 얻어오고, 결과 문자열을 파싱하고,
 * 할인 서버에 질의를 보낼 준비가 되었다.
 * 할인 서버에서 할인율을 확인해서 최종 가격을 계산할 수 있다.
 * (할인 코드와 연계된 할인율은 언제든 바뀔 수 있으므로 매번 서버에서 정보를 얻어 와야 한다.)
 */
public class Quote {
	private final String shopName;
	private final double price;
	private final Discount.Code discountCode;
	
	public Quote(String shopName, double price, Discount.Code discountCode) {
		this.shopName = shopName;
		this.price = price;
		this.discountCode = discountCode;
	}
	
	public static Quote parse(String s) {
		String[] split = s.split(":");
		String shopName = split[0];
		double price = Double.parseDouble(split[1]);
		Discount.Code discountCode = Discount.Code.valueOf(split[2]);
		
		return new Quote(shopName, price, discountCode);
	}
	
	public String getShopName() {
		return this.shopName;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public Discount.Code getDiscountCode() {
		return this.discountCode;
	}
}
