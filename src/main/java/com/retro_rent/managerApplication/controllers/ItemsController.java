package com.retro_rent.managerApplication.controllers;

import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.Dao.*;
import com.retro_rent.managerApplication.exception.ResourceNotFoundException;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.payload.*;
import com.retro_rent.managerApplication.modle.*;
import com.retro_rent.managerApplication.payload.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemsController {

    @Qualifier("CategoryTypeRepository")
    @Autowired
    private CategoryTypeDao categoryTypeRepository;


    @Qualifier("ItemCategoryRepository")
    @Autowired
    private ItemCategoryDao itemCategoryRepository;

    @Qualifier("ItemRepository")
    @Autowired
    private ItemDao itemRepository;

    @Qualifier("OrderedItemRepository")
    @Autowired
    private OrderedItemDao orderedItemRepository;

    @Qualifier("UserRepository")
    @Autowired
    private UserDao userRepository;

    @Autowired
    private ItemReviewDao itemReviewDao;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/items/getMainCategoryOptions")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER', 'TENANT')")
    public List<String> getMainCategoryOptions() {
        List<CategoryType> categoryTypes = categoryTypeRepository.findAllByType(1);
        List<String> returnResult = new LinkedList<>();

        for (CategoryType current : categoryTypes) {
            returnResult.add(current.getName());
        }
        return returnResult;
    }

    @PostMapping("/items/getSecondaryCategoryOptions")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER', 'TENANT')")
    public List<String> getSecondaryCategoryOptions(@RequestBody SecCategoryRequest mainCategory) {

        List<ItemCategory> itemCategories = itemCategoryRepository.findAllByFCategory(categoryTypeRepository.findByName(mainCategory.getMain_category()).get());

        List<String> categoryTypes = new LinkedList<>();
        for (ItemCategory itemCategory: itemCategories) {
            if (!categoryTypes.contains(itemCategory.getsCategory().getName())) {
                categoryTypes.add(itemCategory.getsCategory().getName());
            }
        }

        return categoryTypes;
    }


    @PostMapping("/items/getThirdCategoryOptions")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER', 'TENANT')")
    public List<String> getThirdCategoryOptions(@Valid @RequestBody thirdCategoryRequest thirdCategoryRequest) {

        List<ItemCategory> itemCategories = itemCategoryRepository.findAllByFCategoryAndAndSCategory(categoryTypeRepository.findByName(thirdCategoryRequest.getMain_category()).get(), categoryTypeRepository.findByName(thirdCategoryRequest.getSecondary_category()).get());

        List<String> categoryTypes = new LinkedList<>();
        for (ItemCategory itemCategory: itemCategories) {
            if (!categoryTypes.contains(itemCategory.gettCategory().getName()))
            categoryTypes.add(itemCategory.gettCategory().getName());
        }

        return categoryTypes;
    }

    @Qualifier("RenterRepository")
    @Autowired
    private RenterDao renterRepository;


    @PostMapping("/items/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("myImage") MultipartFile[] img_item, @RequestParam("itemID") long itemID, @RequestParam("userID") long userID) {

        for(MultipartFile file : img_item) {
            fileStorageService.storeFile(file, itemID, userID);
        }

        return ResponseEntity.ok(new ApiResponse(true, "upload item image for  user complete"));
    }

    @PostMapping("/items/createItem")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public long createItem(@Valid @RequestBody ItemRequest itemRequest) {

        Item item = new Item();
        item.setDescription(itemRequest.getDescription());
        item.setPricePerDay(Double.parseDouble(itemRequest.getPricePerDay()));
        item.setYear_of_production(Year.parse(itemRequest.getYear_of_production()));
        item.setCurrency(itemRequest.getCurrency());
        Renter renter = renterRepository.findByUser_Id(itemRequest.getRenter_user_id());
        item.setOwner(renter);

        item.setLabels(itemRequest.getLabels());
        item.setItemCategory(itemCategoryRepository.findByFCategoryAndSCategoryAndTCategory(categoryTypeRepository.findByName(itemRequest.getMain_category()).get(), categoryTypeRepository.findByName(itemRequest.getSecondary_category()).get(),categoryTypeRepository.findByName(itemRequest.getThird_category()).get()));

        List<Boolean> booleans = new LinkedList<>();
        booleans.add(itemRequest.isSun());
        booleans.add(itemRequest.isMon());
        booleans.add(itemRequest.isTue());
        booleans.add(itemRequest.isWed());
        booleans.add(itemRequest.isThu());
        booleans.add(itemRequest.isFri());
        booleans.add(itemRequest.isSat());

        item.setAvailable_day(booleans.toString());
        itemRepository.save(item);

        return item.getId();
    }


    @PostMapping("items/getAvailable")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER', 'TENANT')")
    public @ResponseBody getAvailableResponse getAvailable(@RequestBody getAvR itemID)
    {
        Item item = itemRepository.findById(itemID.getItemID());

        getAvailableResponse response = new getAvailableResponse();
        List<Integer> integers = new ArrayList<>();
        if (item != null) {
            int index = 0;
            for(String s : item.getAvailable_day().split(",")){
                if(s.trim().contains("true")) {
                    integers.add(index);
                }

                index++;
            }

            response.setDaysAvailable(integers);
        }

        List<OrderedItem> orderedItems = orderedItemRepository.findAllByItem_Id(itemID.getItemID());

        if (orderedItems != null) {
            for (OrderedItem orderedItem :
                    orderedItems) {
                if (!(orderedItem.getOrderItemStatus().equals(OrderItemStatus.CANCELED) || orderedItem.getOrderItemStatus().equals(OrderItemStatus.REJECTED))) {
                    response.addDates(orderedItem.getRentalStartDay(), orderedItem.getRentalEndDay());
                }
            }
        }

        return response;
    }

    @PostMapping("/items/editItem")
    @PostAuthorize("hasAnyRole('BOTH', 'RENTER')")
    public ResponseEntity<?> editItem(@Valid @RequestBody EditItemRequest editItemRequest) {
        Item item = itemRepository.findById(editItemRequest.getItemID());

        if (item == null) {
            return ResponseEntity.ok(new ApiResponse(false, "edit item did not work complete"));
        }

        item.setDescription(editItemRequest.getDescription());
        item.setPricePerDay(Double.parseDouble(editItemRequest.getPricePerDay()));
        item.setCurrency(editItemRequest.getCurrency());
        item.setLabels(editItemRequest.getLabels());

        itemRepository.save(item);

        return ResponseEntity.ok(new ApiResponse(true, "edit item complete"));
    }


    @GetMapping(value = "/items/imageURL/{userID}/{itemID}")
    public @ResponseBody List<String> getImageUrles(@PathVariable long userID, @PathVariable long itemID) {

        File file = new File(fileStorageService.getFileStorageLocation().toString() + File.separator + String.valueOf(userID) + File.separator + String.valueOf(itemID));
        File[] listOfFiles = file.listFiles();
        List<String> returnUrls = new LinkedList<>();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    returnUrls.add(listOfFiles[i].getName());
                }
            }
        }

        return returnUrls;
    }


    @GetMapping(value = "/items/image/{userID}/{itemID}/{imageName}",produces = "image/*")
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable long userID, @PathVariable long itemID, @PathVariable String imageName) throws IOException {
        File pic = new File(fileStorageService.getFileStorageLocation().toString() + File.separator + String.valueOf(userID) + File.separator + String.valueOf(itemID) + File.separator + imageName);

        InputStream in = new FileInputStream(pic);
        byte[] bytes = IOUtils.toByteArray(in);

        in.close();
        return bytes;
    }

    @Qualifier("TenantRepository")
    @Autowired
    private TenantDao tenantRepository;


    @GetMapping("/items/getAllItems/{userID}")
    public @ResponseBody List<RenterItemResponse> getAllItems(@PathVariable long userID) {
        List<Item> items = itemRepository.findAll();

        List<Item> wishListUser = null;
        Optional<User> user = userRepository.findById(userID);

        if (user.isPresent()) {
            Tenant tenant = tenantRepository.findByUser_Id(userID);

            if (tenant != null)
            {
                wishListUser = tenant.getWishItems();
            }
        }

        List<RenterItemResponse> renterItemResponse = new LinkedList<>();
        for (Item cur :
                items) {
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

            boolean star = false;
            if (wishListUser != null && wishListUser.size() > 0) {
                star = wishListUser.contains(cur);
            }

            List<ItemReview> itemReviews = itemReviewDao.findAllByItem_Id(cur.getId());


            List<Review> reviews = new ArrayList<>();

            for (ItemReview itemReview:
                    itemReviews) {
                reviews.add(itemReview.getReview());
            }

            renterItemResponse.add(new RenterItemResponse(cur.getId(), cur.getOwner().getUser().getId(), cur.getLabels(), cur.getDescription(), cur.getYear_of_production().toString(), cur.getPricePerDay(), cur.getCurrency(), cur.getItemCategory().getfCategory().getName(), cur.getItemCategory().getsCategory().getName(), cur.getItemCategory().gettCategory().getName(), cur.getOwner().getUser().getUserName(), cur.getOwner().getUser().getEmail(), returnUrls, integers, start, end, star, reviews));
        }

        return renterItemResponse;
    }

    @PostMapping("/items/getAllItemsOfOwner/{userID}")
    public @ResponseBody List<RenterItemResponse> getAllItemsOfOwner(@PathVariable long userID, @RequestBody OwnerRq ownerEmail) {
        Optional<User> userrenter = userRepository.findByEmail(ownerEmail.getOwnerEmail());

        if (!userrenter.isPresent()) {
            throw new ResourceNotFoundException("User", "id", ownerEmail);
        }

        Renter renter = renterRepository.findByUser(userrenter.get());


        List<Item> items = itemRepository.findAllByOwner(renter);

        List<Item> wishListUser = null;
        Optional<User> user = userRepository.findById(userID);

        if (user.isPresent()) {
            Tenant tenant = tenantRepository.findByUser_Id(userID);

            if (tenant != null)
            {
                wishListUser = tenant.getWishItems();
            }
        }

        List<RenterItemResponse> renterItemResponse = new LinkedList<>();
        for (Item cur :
                items) {
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

            boolean star = false;
            if (wishListUser != null && wishListUser.size() > 0) {
                star = wishListUser.contains(cur);
            }

            List<ItemReview> itemReviews = itemReviewDao.findAllByItem_Id(cur.getId());


            List<Review> reviews = new ArrayList<>();

            for (ItemReview itemReview:
                    itemReviews) {
                reviews.add(itemReview.getReview());
            }

            renterItemResponse.add(new RenterItemResponse(cur.getId(), cur.getOwner().getUser().getId(), cur.getLabels(), cur.getDescription(), cur.getYear_of_production().toString(), cur.getPricePerDay(), cur.getCurrency(), cur.getItemCategory().getfCategory().getName(), cur.getItemCategory().getsCategory().getName(), cur.getItemCategory().gettCategory().getName(), cur.getOwner().getUser().getUserName(), cur.getOwner().getUser().getEmail(), returnUrls, integers, start, end, star, reviews));
        }

        return renterItemResponse;
    }

}

class thirdCategoryRequest
{
    @NotNull
    public String main_category;
    @NotNull
    public String secondary_category;

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getSecondary_category() {
        return secondary_category;
    }

    public void setSecondary_category(String secondary_category) {
        this.secondary_category = secondary_category;
    }
}

class SecCategoryRequest {
    @NotNull
    public String main_category;

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }
}

class getAvailableResponse {
    private List<Integer> daysAvailable;
    private List<Date> start;
    private List<Date> end;

    public List<Integer> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(List<Integer> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    public List<Date> getStart() {
        return start;
    }

    public void setStart(List<Date> start) {
        this.start = start;
    }

    public List<Date> getEnd() {
        return end;
    }

    public void setEnd(List<Date> end) {
        this.end = end;
    }

    public void addDates(Date start, Date end)
    {
        if (this.start == null) {
            this.start = new LinkedList<>();
        }

        if (this.end == null) {
            this.end = new LinkedList<>();
        }

        this.start.add(start);
        this.end.add(end);
    }
}

class OwnerRq {
    private String ownerEmail;

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}

class getAvR {
    private long itemID;

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }
}
