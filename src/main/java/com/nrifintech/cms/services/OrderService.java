package com.nrifintech.cms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.utils.Validator;

@Service
public class OrderService implements Validator {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private FeedBackService feedBackService;

	@Autowired
	private ItemService itemService;

	public List<Order> addOrders(List<Order> orders) {

		return orderRepo.saveAll(orders);
	}

	public Order addNewOrder(MealType orderType) {

		Order order = new Order();
		order.setOrderType(orderType);
		
		return orderRepo.save(order);
	}

	public Order getOrder(Integer id) {

		return orderRepo.findById(id).orElse(null);
	}

	public List<Order> getOrders() {

		return orderRepo.findAll();
	}

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

	public Object addFeedBackToOrder(Integer orderId, FeedBack feedBack) {

		Order order = this.getOrder(orderId);

		if (isNotNull(order)) {

			FeedBack f = order.getFeedBack();

			if (isNull(f)) {
				String trimmedComment = feedBack.getComments().toString().trim();
				feedBack.setComments(trimmedComment);

				feedBackService.addFeedBack(feedBack);

				order.setFeedBack(feedBack);
				orderRepo.save(order);

			} else
				return f;
		}
		return order;
	}

	public Object addItemsToOrder(Integer orderId, List<String> itemIds, List<String> quantities) {
		Order order = this.getOrder(orderId);

		if (isNotNull(order)) {

			List<Item> exItems = order.getItems();

			if (exItems.isEmpty()) {

				List<Item> items = new ArrayList<>();

				itemIds.forEach(id -> {

					Item item = itemService.getItem(Integer.valueOf(id));

					if (isNotNull(item)) {
						items.add(new Item(item, Integer.valueOf(quantities.get(itemIds.lastIndexOf(id)))));
					}

				});

				order.setItems(items);
				orderRepo.save(order);

			} else
				return exItems.isEmpty() ? null : exItems;
		}
		return order;
	}

}
