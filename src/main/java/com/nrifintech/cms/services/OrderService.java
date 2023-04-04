package com.nrifintech.cms.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nrifintech.cms.dtos.UrlQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.utils.Validator;

/**
 * This class is used to add, get, update and delete orders
 */
@Service
public class OrderService implements Validator {

	private static final Integer SERVER_LIMIT_MILLIS = 86400000; //difference in millis for 1 day
	private static final LocalTime SERVING_TIME = LocalTime.parse("07:00");


	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private FeedBackService feedBackService;

	@Autowired
	private ItemService itemService;

	/**
	 * It takes a list of orders, saves them to the database, and returns the saved orders
	 *
	 * @param orders The list of orders to be added.
	 * @return A list of orders
	 */
	public List<Order> addOrders(List<Order> orders) {

		return orderRepo.saveAll(orders);
	}

	/**
	 * It saves an order to the database
	 *
	 * @param order The order object that is to be saved.
	 * @return The order object is being returned.
	 */
	public Order saveOrder(Order order) {

		return orderRepo.save(order);
	}

	/**
	 * This function creates a new order and sets the order type to the order type passed in
	 *
	 * @param orderType The type of meal that the order is for.
	 * @return A new Order object is being returned.
	 */
	public Order addNewOrder(MealType orderType) {

		Order order = new Order();
		order.setOrderType(orderType);

		return order;
	}

	/**
	 * If the order exists, return it. If it doesn't, throw an exception
	 *
	 * @param id The id of the order you want to retrieve.
	 * @return The order object is being returned.
	 */
	public Order getOrder(Integer id) {

		return orderRepo.findById(id).orElseThrow(() -> new NotFoundException("Order"));
	}

	/**
	 * > This function returns a list of all the orders in the database
	 *
	 * @return A list of all the orders in the database.
	 */
	public List<Order> getOrders() {

		return orderRepo.findAll();
	}

	/**
	 * > This function takes a list of order IDs and returns a list of orders
	 *
	 * @param orderIds List of order ids
	 * @return A list of orders.
	 */
	public List<Order> getOrders(List<String> orderIds) {

		List<Order> orders = new ArrayList<>();

		orderIds.forEach(id -> {

			Order order = orderRepo.findById(Integer.valueOf(id)).orElse(null);

			if (isNotNull(order)) {
				orders.add(order);
			}

		});

		return orders;
	}

	/**
	 * It adds a feedback to an order
	 *
	 * @param orderId The id of the order
	 * @param feedBack
	 * @return The order object is being returned.
	 */
	public Object addFeedBackToOrder(Integer orderId, FeedBack feedBack) {

		Order order = this.getOrder(orderId);

		if (isNotNull(order)) {

			FeedBack f = order.getFeedBack();

			if (isNull(f)) {
				String trimmedComment = feedBack.getComments().trim();
				feedBack.setComments(trimmedComment);

				feedBackService.addFeedBack(feedBack);

				order.setFeedBack(feedBack);
				orderRepo.save(order);

			} else
				return f;
		}
		return order;
	}

	// public Object addItemsToOrder(Integer orderId, List<String> itemIds, List<String> quantities) {
	// 	Order order = this.getOrder(orderId);
	// // public Object addItemsToOrdr(Integer orderId, List<String> itemIds, List<String> quantities) {
	// // 	Order order = this.getOrder(orderId);

	// // 	if (isNotNull(order)) {

	// // 		List<CartItem> exItems = order.getCartItems();

	// // 		if (exItems.isEmpty()) {

	// // 			List<CartItem> items = new ArrayList<>();

	// // 			itemIds.forEach(id -> {

	// // 				Item item = itemService.getItem(Integer.valueOf(id));

	// // 				if (isNotNull(item)) {
	// // 					items.add(new CartItem(item, Integer.valueOf(quantities.get(itemIds.lastIndexOf(id)))));
	// // 				}

	// // 			});

	// // 			order.setCartItems(items);
	// // 			orderRepo.save(order);

	// // 		} else
	// // 			return exItems.size() > 0 ? new Item() : null;
	// // 	}
	// // 	return order;
	// }

	/**
	 * It takes a date as a parameter and then calls the autoArchive function in the orderRepo class
	 *
	 * @param date the date to be used to archive orders
	 */
	public void autoArchive(String date) {
		orderRepo.autoArchive(date);
	}

	/**
	 * If the difference between the current timestamp and the previous timestamp is less than or equal to
	 * the server limit, then return true
	 *
	 * @param prev The timestamp of the previous request
	 * @param curr The current timestamp
	 * @return A Boolean value.
	 */
	public Boolean getServerEpoch(Timestamp prev, Timestamp curr) {

//		Timestamp curr = Timestamp.valueOf("2023-03-13 12:00:00");
//		Timestamp prev = Timestamp.valueOf("2023-03-12 12:00:00");

		return curr.getTime() - prev.getTime() <= SERVER_LIMIT_MILLIS;

	}

/**
 * It takes a date as input and returns a map of item names and their quantities ordered on that date
 * 
 * @param date The date for which you want to get the order quantity.
 * @return A map of the item name and the quantity of that item that was ordered on the given date.
 */
public Map<String, UrlQuantity> getOrderQuantity(Date date){

	Map<String , UrlQuantity> result = new HashMap<>();

	List<Order>  orders = orderRepo.findByOrderPlaced(date.toString());
	if(!orders.isEmpty()) {
		orders.stream().forEach(o -> o.getCartItems().forEach(item->{
			if( result.get(item.getName()) == null ){
				result.put(item.getName() , new UrlQuantity(item.getImagePath() , item.getQuantity()));
			}
			else {
				result.get(item.getName()).setCount(result.get(item.getName()).getCount() + item.getQuantity());
			}

		}));
	}
	orders.stream().forEach(o->o.getCartItems().forEach(item->System.out.println(item)));
	return(result);
}

}
