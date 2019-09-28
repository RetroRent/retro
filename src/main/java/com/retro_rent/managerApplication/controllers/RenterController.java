package com.retro_rent.managerApplication.controllers;

import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.exception.ResourceNotFoundException;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.payload.*;
import com.retro_rent.managerApplication.security.CurrentUser;
import com.retro_rent.managerApplication.security.UserPrincipal;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.payload.ApiResponse;
import com.retro_rent.managerApplication.payload.FileStorageService;
import com.retro_rent.managerApplication.payload.OrderedItemRenterRespons;
import com.retro_rent.managerApplication.payload.RenterItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@RestController
public class RenterController {
    @Qualifier("UserRepository")
    @Autowired
    private UserDao userRepository;

    @Qualifier("RenterRepository")
    @Autowired
    private RenterDao renterRepository;

    @Qualifier("TenantRepository")
    @Autowired
    private TenantDao tenantRepository;


    @Autowired
    private MessageBoxDao messageBoxDao;

    @Qualifier("ItemRepository")
    @Autowired
    private ItemDao itemRepository;

    @Qualifier("OrderedItemRepository")
    @Autowired
    private OrderedItemDao orderedItemRepository;

    @Autowired
    private UserReviewDao userReviewDao;

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ItemReviewDao itemReviewDao;

    @GetMapping("/renter/getAllItems")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public @ResponseBody List<RenterItemResponse> getAllItems(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser(user.get());
        List<Item> items = itemRepository.findAllByOwner(renter);

        List<RenterItemResponse> renterItemRespons = new LinkedList<>();
        for (Item cur :
                items) {
            File file = new File(fileStorageService.getFileStorageLocation().toString() + File.separator + String.valueOf(userPrincipal.getId()) + File.separator + String.valueOf(cur.getId()));
            File[] listOfFiles = file.listFiles();
            List<String> returnUrls = new LinkedList<>();
            if (listOfFiles != null ) {
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

            renterItemRespons.add(new RenterItemResponse(cur.getId(), cur.getOwner().getUser().getId(), cur.getLabels(), cur.getDescription(), cur.getYear_of_production().toString(), cur.getPricePerDay(), cur.getCurrency(), cur.getItemCategory().getfCategory().getName(), cur.getItemCategory().getsCategory().getName(), cur.getItemCategory().gettCategory().getName(), cur.getOwner().getUser().getUserName(), cur.getOwner().getUser().getEmail(), returnUrls, integers, start, end, false, reviews));
        }

        return renterItemRespons;

    }


    @GetMapping("/tenant/allreview/{tenantEmail}")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public @ResponseBody List<Review>  allreview(@PathVariable String tenantEmail)
    {
        Optional<User> user = userRepository.findByEmail(tenantEmail);

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "Email", tenantEmail);
        }

        Tenant tenant = tenantRepository.findByUser_Email(tenantEmail);

        if (tenant == null) {
            return null;
        }

        List<UserReview> allByUser = userReviewDao.findAllByUser(user.get());
        List<Review> reviews = new ArrayList<>();
        for (UserReview userReview :
                allByUser) {
            reviews.add(userReview.getReview());
        }

        return reviews;
    }

    @PostMapping("/renter/reviewTenant")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public ResponseEntity<?> reviewTenant(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ReviewTenant reviewTenant)
    {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Tenant tenant = tenantRepository.findByUser_Email(reviewTenant.getTenantEmail());

        if (tenant == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a tenant"));
        }


        Review review = new Review();
        review.setGiven_by(user.get());
        review.setRank(reviewTenant.getRankStars());
        review.setText(reviewTenant.getTextReview());
        review.setGivenOn(Date.valueOf(LocalDate.now()));
        reviewDao.save(review);

        UserReview userReview = new UserReview();
        userReview.setReview(review);
        userReview.setUser(tenant.getUser());

        userReviewDao.save(userReview);

        return ResponseEntity.ok(new ApiResponse(true, "Tenant review send"));
    }

    @GetMapping("/renter/getAllOrderItemWaiting")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public @ResponseBody List<OrderedItemRenterRespons> getAllOrderItemsWaiting(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser(user.get());
        List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Owner_IdAndOrderItemStatus(renter.getId(), OrderItemStatus.WAITING);
        List<OrderedItemRenterRespons> orderedItemRenterRespons = new ArrayList<>();

        for (OrderedItem orderedItem :
                orderedItems) {
            orderedItemRenterRespons.add(new OrderedItemRenterRespons(orderedItem.getOrderdItemId(), orderedItem.getItem().getId(), orderedItem.getTotalDaysRent(), String.valueOf(orderedItem.getTotalPriceRent()).concat(orderedItem.getItem().getCurrency()), orderedItem.getRentalStartDay(), orderedItem.getRentalEndDay(), orderedItem.getOrderItemStatus().name(), orderedItem.getTenant().getUser().getUserName(), orderedItem.getTenant().getUser().getEmail()));
        }

        return orderedItemRenterRespons;
    }


    @GetMapping("/renter/getAllOrderItemApproved")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public @ResponseBody List<OrderedItemRenterRespons> getAllOrderItemsApproved(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser(user.get());
        List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Owner_IdAndOrderItemStatus(renter.getId(), OrderItemStatus.APPROVED);
        List<OrderedItemRenterRespons> orderedItemRenterRespons = new ArrayList<>();

        for (OrderedItem orderedItem :
                orderedItems) {
            orderedItemRenterRespons.add(new OrderedItemRenterRespons(orderedItem.getOrderdItemId(), orderedItem.getItem().getId(), orderedItem.getTotalDaysRent(), String.valueOf(orderedItem.getTotalPriceRent()).concat(orderedItem.getItem().getCurrency()), orderedItem.getRentalStartDay(), orderedItem.getRentalEndDay(), orderedItem.getOrderItemStatus().name(), orderedItem.getTenant().getUser().getUserName(), orderedItem.getTenant().getUser().getEmail()));
        }

        return orderedItemRenterRespons;
    }

    @GetMapping("/renter/getAllOrders")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public @ResponseBody List<OrderedItemRenterRespons> getAllOrders(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser(user.get());
        List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Owner_IdAndOrderItemStatusIsNot(renter.getId(), OrderItemStatus.CART);
        List<OrderedItemRenterRespons> orderedItemRenterRespons = new ArrayList<>();

        for (OrderedItem orderedItem :
                orderedItems) {
            orderedItemRenterRespons.add(new OrderedItemRenterRespons(orderedItem.getOrderdItemId(), orderedItem.getItem().getId(),  orderedItem.getTotalDaysRent(), String.valueOf(orderedItem.getTotalPriceRent()).concat(orderedItem.getItem().getCurrency()), orderedItem.getRentalStartDay(), orderedItem.getRentalEndDay(), orderedItem.getOrderItemStatus().name(), orderedItem.getTenant().getUser().getUserName(), orderedItem.getTenant().getUser().getEmail()));
        }

        return orderedItemRenterRespons;
    }

    @GetMapping("/renter/approveOrder/{orderItemID}")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public ResponseEntity<?> getOrderItemsApprove(@CurrentUser UserPrincipal userPrincipal, @PathVariable long orderItemID) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser(user.get());
        OrderedItem orderedItems = orderedItemRepository.findByItem_Owner_IdAndOrderItemStatusAndOrderdItemId(renter.getId(), OrderItemStatus.WAITING, orderItemID);

        orderedItems.setOrderItemStatus(OrderItemStatus.APPROVED);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Your order have been approved, order number : ").append(orderedItems.getOrderdItemId());
        stringBuilder.append("\n");

        sendMail(orderedItems.getTenant().getUser().getEmail(), "Approve Order", stringBuilder.toString());


        MessageBox messageBox2 = new MessageBox();
        messageBox2.setStatus(MessageBoxStatus.WAITING);
        messageBox2.setText(stringBuilder.toString());
        messageBox2.setUser(orderedItems.getTenant().getUser());
        messageBox2.setDate(Date.valueOf(LocalDate.now()));
        messageBox2.setMessageTitle("Approve Order");
        messageBoxDao.save(messageBox2);


        orderedItemRepository.save(orderedItems);

        return ResponseEntity.ok(new ApiResponse(true, "order approved"));
    }

    @GetMapping("/renter/finishOrder/{orderItemID}")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public ResponseEntity<?> getOrderItemsFinish(@CurrentUser UserPrincipal userPrincipal, @PathVariable long orderItemID) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser(user.get());
        OrderedItem orderedItems = orderedItemRepository.findByItem_Owner_IdAndOrderItemStatusAndOrderdItemId(renter.getId(), OrderItemStatus.APPROVED, orderItemID);

        orderedItems.setOrderItemStatus(OrderItemStatus.FINISHED);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Your order has been finished, order number : ").append(orderedItems.getOrderdItemId());
        stringBuilder.append("\n").append("You can now enter the website and give a review");

        sendMail(orderedItems.getTenant().getUser().getEmail(), "Finished Order", stringBuilder.toString());


        MessageBox messageBox2 = new MessageBox();
        messageBox2.setStatus(MessageBoxStatus.WAITING);
        messageBox2.setText(stringBuilder.toString());
        messageBox2.setUser(orderedItems.getTenant().getUser());
        messageBox2.setDate(Date.valueOf(LocalDate.now()));
        messageBox2.setMessageTitle("Finished Order");

        messageBoxDao.save(messageBox2);


        orderedItemRepository.save(orderedItems);

        return ResponseEntity.ok(new ApiResponse(true, "order approved"));
    }


    public void sendMail(String email, String title, String text) {
        sendEmail(email, title, text, javaMailSender);

    }

    public static void sendEmail(String email, String title, String text, JavaMailSender javaMailSender) {
        SimpleMailMessage msg = new SimpleMailMessage();


        msg.setFrom("retrorent4all@gmail.com");

        msg.setTo(email);

        msg.setSubject(title);

        msg.setText(text);

        javaMailSender.send(msg);
    }


    @GetMapping("/renter/rejectOrder/{orderItemID}")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public ResponseEntity<?> getOrderItemsReject(@CurrentUser UserPrincipal userPrincipal, @PathVariable long orderItemID) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }

        Renter renter = renterRepository.findByUser(user.get());
        OrderedItem orderedItems = orderedItemRepository.findByItem_Owner_IdAndOrderItemStatusAndOrderdItemId(renter.getId(), OrderItemStatus.WAITING, orderItemID);

        orderedItems.setOrderItemStatus(OrderItemStatus.REJECTED);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Your order have been rejected, order number : ").append(orderedItems.getOrderdItemId());
        stringBuilder.append("\n");

        sendMail(orderedItems.getTenant().getUser().getEmail(), "Rejected Order", stringBuilder.toString());


        MessageBox messageBox2 = new MessageBox();
        messageBox2.setStatus(MessageBoxStatus.WAITING);
        messageBox2.setText(stringBuilder.toString());
        messageBox2.setUser(orderedItems.getTenant().getUser());
        messageBox2.setDate(Date.valueOf(LocalDate.now()));
        messageBox2.setMessageTitle("Rejected Order");

        messageBoxDao.save(messageBox2);


        orderedItemRepository.save(orderedItems);

        return ResponseEntity.ok(new ApiResponse(true, "order rejected"));
    }


    @PostMapping("/renter/deleteItemPic/{imageName}")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public ResponseEntity<?> deleteItemPic(@Valid @RequestBody deleteRequest deleteRequest, @PathVariable String imageName) {
        Optional<User> user = userRepository.findById(deleteRequest.getUserID());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",deleteRequest.getUserID());
        }

        Renter renter = renterRepository.findByUser(user.get());

        if (renter == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a renter"));
        }

        try {
            fileStorageService.deleteFile(user.get().getId(), deleteRequest.getItemID(), imageName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "can not delete this item picture"));
        }

        return ResponseEntity.ok(new ApiResponse(true, "item pic deleted"));
    }

    @PostMapping("/renter/deleteItem")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public ResponseEntity<?> deleteItem(@Valid @RequestBody deleteRequest deleteRequest) {
        Optional<User> user = userRepository.findById(deleteRequest.getUserID());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "id",deleteRequest.getUserID());
        }

        Renter renter = renterRepository.findByUser(user.get());

        if (renter == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not a renter"));
        }

        Item items = itemRepository.findByOwnerAndId(renter, deleteRequest.getItemID());

        if (items == null) {
            return ResponseEntity.ok(new ApiResponse(false, "user is not the owner of the item"));
        }

        List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Id(items.getId());
        if (orderedItems != null && orderedItems.size() > 0)
        {
            return ResponseEntity.ok(new ApiResponse(false, "the item already ordered and can not be deleted"));
        }

        try {
            fileStorageService.deleteFiles(user.get().getId(), items.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "the item picture can not be deleted"));
        }

        itemRepository.delete(items);
        return ResponseEntity.ok(new ApiResponse(true, "item deleted"));
    }
}

class deleteRequest {
    @NotNull
    private long userID;
    @NotNull
    private long itemID;

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

class ReviewTenant {
    private String tenantEmail;
    private int rankStars;
    private String textReview;

    public String getTenantEmail() {
        return tenantEmail;
    }

    public void setTenantEmail(String tenantEmail) {
        this.tenantEmail = tenantEmail;
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

