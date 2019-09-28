package com.retro_rent.managerApplication.controllers;

import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.exception.ResourceNotFoundException;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.payload.*;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.security.CurrentUser;
import com.retro_rent.managerApplication.security.UserPrincipal;
import com.retro_rent.managerApplication.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class TenantController {
    @Qualifier("UserRepository")
    @Autowired
    private UserDao userRepository;

    @Qualifier("TenantRepository")
    @Autowired
    private TenantDao tenantRepository;

    @Qualifier("ItemRepository")
    @Autowired
    private ItemDao itemRepository;

    @Qualifier("OrderedItemRepository")
    @Autowired
    private OrderedItemDao orderedItemRepository;

    @Qualifier("OrderRepository")
    @Autowired
    private OrderDao orderedRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserReviewDao userReviewDao;

    @Autowired
    private ItemReviewDao itemReviewDao;

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private FileStorageService fileStorageService;

    @Qualifier("RenterRepository")
    @Autowired
    private RenterDao renterRepository;

    @Autowired
    private MessageBoxDao messageBoxDao;

    @GetMapping("/tenant/itemCartLength")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public @ResponseBody long itemCartLength(@CurrentUser UserPrincipal userPrincipal)
    {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());
        if (tenant == null) {
            return 0;
        }

        List<OrderedItem> orderedItems = orderedItemRepository.findAllByTenant_TenantIDAndOrderItemStatus(tenant.getTenantID(), OrderItemStatus.CART);
        return orderedItems != null ? (orderedItems.size()) : (0);
    }

    @GetMapping("/tenant/getAllItemsCart")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public @ResponseBody List<OrderItemResponse> getAllItemsCart(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        List<OrderedItem> orderedCartItems = orderedItemRepository.findAllByTenant_TenantIDAndOrderItemStatus(tenant.getTenantID(), OrderItemStatus.CART);

        List<OrderItemResponse> orderItemResponses = new LinkedList<>();
        for (OrderedItem cur :
                orderedCartItems) {
            File file = new File(fileStorageService.getFileStorageLocation().toString() + File.separator + String.valueOf(cur.getItem().getOwner().getUser().getId()) + File.separator + String.valueOf(cur.getItem().getId()));
            File[] listOfFiles = file.listFiles();
            List<String> returnUrls = new LinkedList<>();

            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        returnUrls.add(listOfFiles[i].getName());
                    }
                }
            }

            List<Integer> integers = new ArrayList<>();
            int index = 0;
            for(String s : cur.getItem().getAvailable_day().split(",")){
                if(s.trim().contains("true")) {
                    integers.add(index);
                }

                index++;
            }

            List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Id(cur.getItem().getId());
            List<java.sql.Date> start = new ArrayList<>();
            List<java.sql.Date> end = new ArrayList<>();

            if (orderedItems != null) {
                for (OrderedItem orderedItem :
                        orderedItems) {
                    if (!(orderedItem.getOrderItemStatus().equals(OrderItemStatus.CANCELED) || orderedItem.getOrderItemStatus().equals(OrderItemStatus.REJECTED))) {

                        start.add(orderedItem.getRentalStartDay());
                        end.add(orderedItem.getRentalEndDay());
                    }
                }
            }

            List<ItemReview> itemReviews = itemReviewDao.findAllByItem_Id(cur.getItem().getId());

            List<Review> reviews = new ArrayList<>();

            for (ItemReview itemReview:
                 itemReviews) {
                reviews.add(itemReview.getReview());
            }

            RenterItemResponse renterItemResponse = new RenterItemResponse(cur.getItem().getId(), cur.getItem().getOwner().getUser().getId(), cur.getItem().getLabels(), cur.getItem().getDescription(), cur.getItem().getYear_of_production().toString(), cur.getItem().getPricePerDay(), cur.getItem().getCurrency(), cur.getItem().getItemCategory().getfCategory().getName(), cur.getItem().getItemCategory().getsCategory().getName(), cur.getItem().getItemCategory().gettCategory().getName(), cur.getItem().getOwner().getUser().getUserName(), cur.getItem().getOwner().getUser().getEmail(), returnUrls, integers, start, end, tenant.getWishItems().contains(cur.getItem()), reviews);
            orderItemResponses.add(new OrderItemResponse(cur.getOrderdItemId(), renterItemResponse, cur.getTotalDaysRent(), String.valueOf(cur.getTotalPriceRent()).concat(cur.getItem().getCurrency()), cur.getRentalStartDay(), cur.getRentalEndDay(), OrderItemStatus.CART.name()));
        }

        return orderItemResponses;
    }


    @PostMapping("/tenant/reviewItem")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public ResponseEntity<?> reviewItem(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ReviewItem reviewItem)
    {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser_Email(reviewItem.getOwnerEmail());

        if (renter == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a renter"));
        }

        Item item = itemRepository.findByOwnerAndId(renter, reviewItem.getItemID());


        Review review = new Review();
        review.setGiven_by(user.get());
        review.setRank(reviewItem.getRankStars());
        review.setText(reviewItem.getTextReview());
        review.setGivenOn(Date.valueOf(LocalDate.now()));
        reviewDao.save(review);

        ItemReview itemReview = new ItemReview();
        itemReview.setReview(review);
        itemReview.setItem(item);

        itemReviewDao.save(itemReview);

        return ResponseEntity.ok(new ApiResponse(true, "Item review send"));
    }

    @GetMapping("/tenant/getTenantOrders")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public @ResponseBody List<OrderItemResponse> getTenantOrders(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        List<OrderedItem> orderedCartItems = orderedItemRepository.findAllByTenant_TenantIDAndOrderItemStatusIsNot(tenant.getTenantID(), OrderItemStatus.CART);

        List<OrderItemResponse> orderItemResponses = new LinkedList<>();
        for (OrderedItem cur :
                orderedCartItems) {
            File file = new File(fileStorageService.getFileStorageLocation().toString() + File.separator + String.valueOf(cur.getItem().getOwner().getUser().getId()) + File.separator + String.valueOf(cur.getItem().getId()));
            File[] listOfFiles = file.listFiles();
            List<String> returnUrls = new LinkedList<>();

            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        returnUrls.add(listOfFiles[i].getName());
                    }
                }
            }

            List<Integer> integers = new ArrayList<>();
            int index = 0;
            for(String s : cur.getItem().getAvailable_day().split(",")){
                if(s.trim().contains("true")) {
                    integers.add(index);
                }

                index++;
            }

            List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Id(cur.getItem().getId());
            List<java.sql.Date> start = new ArrayList<>();
            List<java.sql.Date> end = new ArrayList<>();

            if (orderedItems != null) {
                for (OrderedItem orderedItem :
                        orderedItems) {
                    if (!(orderedItem.getOrderItemStatus().equals(OrderItemStatus.CANCELED) || orderedItem.getOrderItemStatus().equals(OrderItemStatus.REJECTED))) {

                        start.add(orderedItem.getRentalStartDay());
                        end.add(orderedItem.getRentalEndDay());
                    }
                }
            }

            List<ItemReview> itemReviews = itemReviewDao.findAllByItem_Id(cur.getItem().getId());


            List<Review> reviews = new ArrayList<>();

            for (ItemReview itemReview:
                    itemReviews) {
                reviews.add(itemReview.getReview());
            }

            RenterItemResponse renterItemResponse = new RenterItemResponse(cur.getItem().getId(), cur.getItem().getOwner().getUser().getId(), cur.getItem().getLabels(), cur.getItem().getDescription(), cur.getItem().getYear_of_production().toString(), cur.getItem().getPricePerDay(), cur.getItem().getCurrency(), cur.getItem().getItemCategory().getfCategory().getName(), cur.getItem().getItemCategory().getsCategory().getName(), cur.getItem().getItemCategory().gettCategory().getName(), cur.getItem().getOwner().getUser().getUserName(), cur.getItem().getOwner().getUser().getEmail(), returnUrls, integers, start, end, tenant.getWishItems().contains(cur.getItem()), reviews);
            orderItemResponses.add(new OrderItemResponse(cur.getOrderdItemId(), renterItemResponse, cur.getTotalDaysRent(), String.valueOf(cur.getTotalPriceRent()).concat(cur.getItem().getCurrency()), cur.getRentalStartDay(), cur.getRentalEndDay(), cur.getOrderItemStatus().name()));
        }

        return orderItemResponses;
    }


    @GetMapping("/tenant/removeFromWishList/{itemID}")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public ResponseEntity<?> removeFromWishList(@CurrentUser UserPrincipal userPrincipal, @PathVariable long itemID) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        if (tenant == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a tenant"));
        }

        Item item = itemRepository.findById(itemID);

        if (item != null) {
            tenant.removeFromeWishList(item);
            tenantRepository.save(tenant);
        }

        return ResponseEntity.ok(new ApiResponse(true, "item removed from wish list"));
    }

    @GetMapping("/tenant/getWishList")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public @ResponseBody List<RenterItemResponse> getWishList(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        if (tenant == null) {
            return null;
        }

        List<RenterItemResponse> renterItemResponse = new LinkedList<>();
        for (Item cur :
                tenant.getWishItems()) {
            File file = new File(fileStorageService.getFileStorageLocation().toString() + File.separator + String.valueOf(cur.getOwner().getUser().getId()) + File.separator + String.valueOf(cur.getId()));
            File[] listOfFiles = file.listFiles();
            List<String> returnUrls = new LinkedList<>();

            if (listOfFiles != null) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        returnUrls.add(listOfFiles[i].getName());
                    }
                }
            }

            List<Integer> integers = new ArrayList<>();
            int index = 0;
            for(String s : cur.getAvailable_day().split(",")){
                if(s.trim().contains("true")) {
                    integers.add(index);
                }

                index++;
            }

            List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Id(cur.getId());
            List<java.sql.Date> start = new ArrayList<>();
            List<java.sql.Date> end = new ArrayList<>();

            if (orderedItems != null) {
                for (OrderedItem orderedItem :
                        orderedItems) {
                    if (!(orderedItem.getOrderItemStatus().equals(OrderItemStatus.CANCELED) || orderedItem.getOrderItemStatus().equals(OrderItemStatus.REJECTED))) {

                        start.add(orderedItem.getRentalStartDay());
                        end.add(orderedItem.getRentalEndDay());
                    }
                }
            }

            List<ItemReview> itemReviews = itemReviewDao.findAllByItem_Id(cur.getId());

            List<Review> reviews = new ArrayList<>();

            for (ItemReview itemReview:
                    itemReviews) {
                reviews.add(itemReview.getReview());
            }

            renterItemResponse.add(new RenterItemResponse(cur.getId(), cur.getOwner().getUser().getId(), cur.getLabels(), cur.getDescription(), cur.getYear_of_production().toString(), cur.getPricePerDay(), cur.getCurrency(), cur.getItemCategory().getfCategory().getName(), cur.getItemCategory().getsCategory().getName(), cur.getItemCategory().gettCategory().getName(), cur.getOwner().getUser().getUserName(), cur.getOwner().getUser().getEmail(), returnUrls, integers, start, end, true, reviews));
        }

        return renterItemResponse;
    }


    @GetMapping("/tenant/addToWishList/{itemID}")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public ResponseEntity<?> addToWishList(@CurrentUser UserPrincipal userPrincipal, @PathVariable long itemID) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        if (tenant == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a tenant"));
        }

        Item item = itemRepository.findById(itemID);

        if (item != null) {
            tenant.addToWishList(item);
            tenantRepository.save(tenant);
        }

        return ResponseEntity.ok(new ApiResponse(true, "item added to wish list"));
    }

    @PostMapping("/tenant/addItemToCart")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody AddCartRequest addCartRequest) {
        Optional<User> user = userRepository.findById(addCartRequest.getUserID());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",addCartRequest.getUserID());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        if (tenant == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a tenant"));
        }

        Item item = itemRepository.findById(addCartRequest.getItemID());

        if (item == null) {
            return ResponseEntity.ok(new ApiResponse(false, "item can not be found"));
        }

        long endTime = addCartRequest.getRentalEndDay().getTime() ; // create your endtime here, possibly using Calendar or Date
        long curTime = addCartRequest.getRentalStartDay().getTime();
        boolean[] days = {false, false, false, false, false, false, false};
        while (curTime <= endTime) {
            Date date = new Date(curTime);
//            1-7 for Monday-Sunday
            int dayV = date.toLocalDate().getDayOfWeek().getValue();
            if (dayV == 7) {
                dayV = 0;
            }

            days[dayV] = true;
            curTime = curTime + 24*60*60*1000;
        }

        int indexD = 0;
        for (String cur : item.getAvailable_day().split(",")) {
            if (cur.contains("false") && days[indexD]) {
                return ResponseEntity.ok(new ApiResponse(false, "Date selected are unavailable"));
            }

            indexD++;
        }

        List<OrderedItem> orderedItemsEx = orderedItemRepository.findAllByItem_Id(item.getId());

        for (OrderedItem orderedItem :
                orderedItemsEx) {
            if (!(orderedItem.getOrderItemStatus().equals(OrderItemStatus.CANCELED) || orderedItem.getOrderItemStatus().equals(OrderItemStatus.REJECTED))) {

                if ((orderedItem.getRentalStartDay().before(addCartRequest.getRentalEndDay()) && orderedItem.getRentalEndDay().after(addCartRequest.getRentalEndDay())) ||
                        (orderedItem.getRentalStartDay().before(addCartRequest.getRentalStartDay()) && orderedItem.getRentalEndDay().after(addCartRequest.getRentalStartDay())) ||
                        (orderedItem.getRentalStartDay().before(addCartRequest.getRentalStartDay()) && orderedItem.getRentalEndDay().after(addCartRequest.getRentalStartDay())) ||
                        (addCartRequest.getRentalStartDay().before(orderedItem.getRentalStartDay()) && addCartRequest.getRentalEndDay().after(orderedItem.getRentalEndDay()))) {
                    return ResponseEntity.ok(new ApiResponse(false, "Date selected are unavailable"));

                }
            }
        }

        long diff = addCartRequest.getRentalEndDay().getTime() - addCartRequest.getRentalStartDay().getTime();
        int daysTotal = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setItem(item);
        orderedItem.setRentalEndDay(addCartRequest.getRentalEndDay());
        orderedItem.setRentalStartDay(addCartRequest.getRentalStartDay());
        orderedItem.setTenant(tenant);
        orderedItem.setTotalPriceRent(daysTotal * item.getPricePerDay());
        orderedItem.setTotalDaysRent(daysTotal);
        orderedItem.setOrderItemStatus(OrderItemStatus.CART);
        orderedItemRepository.save(orderedItem);

        return ResponseEntity.ok(new ApiResponse(true, "item add to Cart"));
    }

    @GetMapping("tenant/deleteOrder/{orderItemID}")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public ResponseEntity<?> deleteOrder(@CurrentUser UserPrincipal userPrincipal, @PathVariable long orderItemID) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        if (tenant == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a tenant"));
        }

        OrderedItem orderedItem = orderedItemRepository.findByTenant_TenantIDAndOrderdItemId(tenant.getTenantID(), orderItemID);

        if (orderedItem == null) {
            return ResponseEntity.ok(new ApiResponse(false, "order item can not be found"));
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You have order that was canceled by the tenant, item number : ").append(orderedItem.getItem().getId());
        stringBuilder.append("\n");
        stringBuilder.append("order number: ").append(orderedItem.getOrderdItemId()).append("\n")
                .append("Tenant : ").append(orderedItem.getTenant().getUser().getUserName()).append(",").append(orderedItem.getTenant().getUser().getEmail());

        sendMail(orderedItem.getItem().getOwner().getUser().getEmail(), "Canceled Order", stringBuilder.toString());

        MessageBox messageBox = new MessageBox();
        messageBox.setStatus(MessageBoxStatus.WAITING);
        messageBox.setText(stringBuilder.toString());
        messageBox.setUser(orderedItem.getItem().getOwner().getUser());
        messageBox.setDate(Date.valueOf(LocalDate.now()));
        messageBox.setMessageTitle("Canceled Order");

        messageBoxDao.save(messageBox);

        orderedItem.setOrderItemStatus(OrderItemStatus.CANCELED);
        orderedItemRepository.save(orderedItem);

        return ResponseEntity.ok(new ApiResponse(true, "order deleted"));
    }


    @GetMapping("/tenant/purchase")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public ResponseEntity<?> purchaseAllCartOrderItems(@CurrentUser UserPrincipal userPrincipal)
    {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        if (tenant == null) {
            return ResponseEntity.ok(new ApiOrderResponse(false, "user is not a tenant", null));
        }

        Order order = new Order();
        order.setTenant(tenant);
        order.setCreationDate(Date.valueOf(LocalDate.now()));

        List<OrderedItem> orderedCartItems = orderedItemRepository.findAllByTenant_TenantIDAndOrderItemStatus(tenant.getTenantID(), OrderItemStatus.CART);

        List<String> payPalMe = new ArrayList<>();
        for (OrderedItem orderedItem:
             orderedCartItems) {
            String pay = String.valueOf(orderedItem.getTotalPriceRent()).concat(orderedItem.getItem().getCurrency());
            payPalMe.add(orderedItem.getItem().getOwner().getUser().getPaymentLink().concat("/".concat(pay)));
            orderedItem.setOrderItemStatus(OrderItemStatus.WAITING);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("You have new order waiting for approval, item number : ").append(orderedItem.getItem().getId());
            stringBuilder.append("\n");
            stringBuilder.append("order created by : ").append(orderedItem.getTenant().getUser().getUserName()).append(", ").append(orderedItem.getTenant()
                    .getUser().getEmail());

            sendMail(orderedItem.getItem().getOwner().getUser().getEmail(), "New Order", stringBuilder.toString());


            MessageBox messageBox = new MessageBox();
            messageBox.setStatus(MessageBoxStatus.WAITING);
            messageBox.setText(stringBuilder.toString());
            messageBox.setUser(orderedItem.getItem().getOwner().getUser());
            messageBox.setDate(Date.valueOf(LocalDate.now()));
            messageBox.setMessageTitle("New Order");

            messageBoxDao.save(messageBox);

            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("You have new order waiting for approval By renter :").append(orderedItem.getItem().getOwner().getUser().getUserName());
            stringBuilder2.append("\n");

            sendMail(orderedItem.getTenant().getUser().getEmail(), "New Order", stringBuilder2.toString());


            MessageBox messageBox2 = new MessageBox();
            messageBox2.setStatus(MessageBoxStatus.WAITING);
            messageBox2.setText(stringBuilder2.toString());
            messageBox2.setUser(orderedItem.getTenant().getUser());
            messageBox2.setDate(Date.valueOf(LocalDate.now()));
            messageBox2.setMessageTitle("New Order");

            messageBoxDao.save(messageBox2);

            orderedItemRepository.save(orderedItem);
        }

        order.setOrderedItems(orderedCartItems);

        orderedRepository.save(order);

        return ResponseEntity.ok(new ApiOrderResponse(true, "order created", payPalMe));
    }

    public void sendMail(String email, String title, String text) {
        RenterController.sendEmail(email, title, text, javaMailSender);
    }

    @PostMapping("/tenant/deleteItemCart")
    @PostAuthorize("hasAnyRole('BOTH', 'TENANT')")
    public ResponseEntity<?> deleteItemFromCart(@Valid @RequestBody DeleteCartRequest deleteCartRequest) {
        Optional<User> user = userRepository.findById(deleteCartRequest.getUserID());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",deleteCartRequest.getUserID());
        }

        Tenant tenant = tenantRepository.findByUser(user.get());

        if (tenant == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a tenant"));
        }

        OrderedItem item = orderedItemRepository.findByOrderdItemId(deleteCartRequest.getOrderItemID());

        if (item == null) {
            return ResponseEntity.ok(new ApiResponse(false, "item can not be found"));
        }

        orderedItemRepository.delete(item);

        return ResponseEntity.ok(new ApiResponse(true, "item deleted from Cart"));
    }
}

class AddCartRequest {
    @NotNull
    private long userID;
    @NotNull
    private long itemID;

    @NotNull
    private Date rentalStartDay;

    @NotNull
    private Date rentalEndDay;

    public Date getRentalStartDay() {
        return rentalStartDay;
    }

    public void setRentalStartDay(Date rentalStartDay) {
        this.rentalStartDay = rentalStartDay;
    }

    public Date getRentalEndDay() {
        return rentalEndDay;
    }

    public void setRentalEndDay(Date rentalEndDay) {
        this.rentalEndDay = rentalEndDay;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }
}
class DeleteCartRequest {
    @NotNull
    private long userID;
    @NotNull
    private long orderItemID;

    public long getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(long orderItemID) {
        this.orderItemID = orderItemID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}

class ReviewItem {
    private String ownerEmail;
    private long itemID;
    private int rankStars;
    private String textReview;

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public int getRankStars() {
        return rankStars;
    }

    public void setRankStars(int rankStars) {
        this.rankStars = rankStars;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }
}
