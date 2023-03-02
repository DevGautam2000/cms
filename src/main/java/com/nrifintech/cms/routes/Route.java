package com.nrifintech.cms.routes;

public class Route {
	public interface Admin {
		String prefix = "/admin/";
		
		String getAdmin = "getadmin";
	}

	public interface User {
		String prefix = "/user/";
		
		String getUser = "getuser";
		String addUser = "adduser";
		String removeUser = "removeuser";
		String placeOrder = "placeorder";

		String getUsers = "getusers";

	}

	public interface Menu {
		String prefix = "/menu/";

		String getMenu = "getmenu";
		String addMenu = "addmenu";
		String addToMenu = "addtomenu";
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
		
		String addOrders = "addorders";
		String getOrders = "getorders";
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

}
