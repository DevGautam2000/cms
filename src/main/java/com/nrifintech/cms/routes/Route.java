package com.nrifintech.cms.routes;

/**
 * It contains all the routes that are used in the application
 */
public class Route {
	public static final String root="http://localhost:8080";
	// Defining a route for the admin.
	public interface Admin {
		String prefix = "/admin/";

		String getAdmin = "getadmin";
	}

	// Defining the routes for the user.
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

	// Defining the routes for the menu.
	public interface Menu {
		String prefix = "/menu/";

		String getMenu = "getmenu";
		String addMenu = "addmenu";
		String removeFromMenu = "removefrommenu";
		String getMonthMenu = "getmonthmenu";

		String removeMenu = "removemenu";
		String approveMenu = "approvemenu";

		String getByDate = "getbydate";

		String submitMenu = "submitmenu";
	}

	// Defining the routes for the item.
	public interface Item {
		String prefix = "/item/";

		String getItems = "getitems";
		String getItem = "getitem";
		String addItem = "additem";
		String addItems = "additems";
	}

	// Defining the routes for the feedback.
	public interface FeedBack {
		String prefix = "/feedback/";

		String addFeedback = "addfeedback";
		String getFeedback = "getfeedback";
	}

	// Defining the routes for the order.
	public interface Order {
		String prefix = "/order/";

		String addOrders = "addorders";//na
		String getOrders = "getorders";

		String updateStatus = "updatestatus";

		String placeOrder = "placeorder";

		String cancelOrder = "cancelorder";

		String getOrderQuantity  = "getorderquantity";

	
	}

	// Defining the routes for the cart.
	public interface Cart {
		String prefix = "/cart/";

		String addToCart = "addtocart";
		String getCart = "getcart";

		String updateQuantity = "updatequantity";

		String remove = "remove";

		String clear = "clear";
	}

	// Used to define the routes for the cart item.
	public interface CartItem {
		String prefix = "/cartitem/";

		String addItem = "additem";
		String getItem = "getitem";
		String addItems = "additems";
		String getItems = "getitems";
	}


	// Used to define the routes for the authentication.
	public interface Authentication{
		String prefix = "/auth/";
		
		String generateToken = "generate-token";
		String currentUser = "current-user";
		String forgotPassword = "forgot-password";
		String changePassword = "change-password";
		String setNewPassword = "set-new-password";
		String activateNewPassword = "activate-new-password";


	}

	// Defining the routes for the wallet.
	public interface Wallet {
		String prefix = "/wallet/";
		String getWallet = "getwallet";
		String addMoney = "addmoney";
	}

	// Defining the routes for the inventory.
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

	// Defining the routes for the purchase.
	public interface Purchase {
		String prefix = "/purchase/";
		String save = "save";
		String get = "get/";
		String rollback = "rollback/";
	}

	// Defining the routes for the analytics.
	public interface Analytics{
		String prefix = "/analytics/";
		String getTotalExp = "totalexp";
		String getDateWiseExp = "datebydateexp";
		String getTotalSales = "totalsales";
		String getDateWiseSales = "datebydatesales";
		String getBestSeller = "bestseller";
		String getOrderStats = "orderstats";
		String getFeedbackStats = "feedbackstats";
		String getUserStats = "userstats";
	}

	// Defining the routes for the excel.
	public interface Excel {
		String prefix  = "/excel/";

		String getUserReports = "getuserreports";
		String getMenuReports = "getmenureports";
	}


}
