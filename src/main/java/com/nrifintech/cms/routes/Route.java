package com.nrifintech.cms.routes;

public class Route {
	public interface Admin {
		String prefix = "/admin";
		String getAdmin = prefix +"/" + "getadmin";
	}

	public interface User {

		String getUser = "getuser";
		String addUser = "adduser";
		String removeUser = "removeuser";

	}

	public interface Menu {
		String prefix = "/menu";

		String getMenu = "getmenu";
		String addMenu = "addmenu";
		String addToMenu = "addtomenu";
		String removeFromMenu = "removefrommenu";
		String getMonthMenu = "getmonthmenu";

		String removeMenu = "removemenu";
		String approveMenu = "approvemenu";

		String addItemsToMenu = "additemstomenu";
	}

	public interface Item {
		String getItems = "getitems";
		String getItem = "getitem";
		String addItem = "additem";
	}

}
