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
		String subscriptionToggler = "subscriptiontoggle";
		String getEmailStatus = "getemailstatus";


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

		String cancelOrder = "cancelorder";

	
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
		String currentUser = "current-user";
		String forgotPassword = "forgot-password";
		String changePassword = "change-password";
		String setNewPassword = "set-new-password";
		String activateNewPassword = "activate-new-password";


	}

	public interface Wallet {
		String prefix = "/wallet/";
		String getWallet = "getwallet";
		String addMoney = "addmoney";
	}

	public interface Inventory{
		String prefix = "/inventory/";
		String saveOne = "save/one";
		String saveAll = "save/all";
		String get = "get/";
		String getByName = "get/name/";
		String getById = "get/id/";
		String remove = "remove/";
		String updateQtyReq = "update/qtyreq/";
		String updateQtyInHand = "update/qtyhand/";
	}

	public interface Purchase {
		String prefix = "/purchase/";
		String save = "save";
		String get = "get/";
		String rollback = "rollback/";
	}

	public interface Analytics{
		String prefix = "/analytics/";
		String getTotalExp = "totalexp";
		String getDateWiseExp = "datebydateexp";
		String getTotalSales = "totalsales";
		String getDateWiseSales = "datebydatesales";
		String getBestSeller = "bestseller";
		String getOrderStats = "orderstats";
	}


}
