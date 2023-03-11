package com.nrifintech.cms.routes;

public class Route {
	public static final String root="http://localhost:8080";
	public interface Admin {
		String prefix = "/admin/";

		String getAdmin = "getadmin";
	}

	public interface User {
		String prefix = "/user/";

		String addUser = "adduser";
		String removeUser = "removeuser";

		String getUsers = "getusers";
		String getOrders = "getorders";

		String updateStatus = "updatestatus";
		
		String getAllUsersForOrderByDate = "usersbydate";


	}

	public interface Menu {
		String prefix = "/menu/";

		String getMenu = "getmenu";
		String addMenu = "addmenu";
		String removeFromMenu = "removefrommenu";
		String getMonthMenu = "getmonthmenu";

		String removeMenu = "removemenu";
		String approveMenu = "approvemenu";

		String getByDate = "getbydate";
	}

	public interface Item {
		String prefix = "/item/";

		String getItems = "getitems";
		String getItem = "getitem";
		String addItem = "additem";
		String addItems = "additems";
	}

	public interface FeedBack {
		String prefix = "/feedback/";

		String addFeedback = "addfeedback";
		String getFeedback = "getfeedback";
	}

	public interface Order {
		String prefix = "/order/";

		String addOrders = "addorders";//na
		String getOrders = "getorders";

		String updateStatus = "updatestatus";

		String placeOrder = "placeorder";

	
	}

	public interface Cart {
		String prefix = "/cart/";

		String addToCart = "addtocart";
		String getCart = "getcart";

		String updateQuantity = "updatequantity";

		String remove = "remove";

		String clear = "clear";
	}

	public interface CartItem {
		String prefix = "/cartitem/";

		String addItem = "additem";
		String getItem = "getitem";
		String addItems = "additems";
		String getItems = "getitems";
	}


	public interface Authentication{
		String prefix = "/auth/";
		
		String generateToken = "generate-token";
	}

	public interface Wallet {
		String prefix = "/wallet/";
		String getWallet = "getwallet";
		String addMoney = "addmoney";
	}


}
