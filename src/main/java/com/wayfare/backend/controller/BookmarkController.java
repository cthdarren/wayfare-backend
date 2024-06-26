package com.wayfare.backend.controller;

import com.wayfare.backend.model.Bookmark;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.repository.BookmarksRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.request.ListingIdRequest;
import com.wayfare.backend.response.BookmarkResponse;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class BookmarkController {
    private final TourRepository tourRepo;
    private final BookmarksRepository bookmarkRepo;

    public BookmarkController(TourRepository tourRepo, BookmarksRepository bookmarkRepo) {
        this.tourRepo = tourRepo;
        this.bookmarkRepo = bookmarkRepo;
    }

    @GetMapping("/isbookmarked/{listingId}")
    public ResponseObject isBookmarked(@PathVariable String listingId){
        WayfareUserDetails user = getCurrentUserDetails();
        return new ResponseObject(true, bookmarkRepo.existsByListingIdAndUserId(listingId, user.getId()));
    }

    @GetMapping("/getbookmarks")
    public ResponseObject getBookmarks(){
        WayfareUserDetails user = getCurrentUserDetails();
        List<BookmarkResponse> bookmarkList = bookmarkRepo.findAllBookmarks(user.getId());
        return new ResponseObject(true, bookmarkList);
    }

    @PostMapping("/bookmark")
    public ResponseObject bookmarkedListing(@RequestBody ListingIdRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        Optional<TourListing> listing = tourRepo.findById(request.listingId());
        if (listing.isPresent()){
            if (bookmarkRepo.existsByListingIdAndUserId(request.listingId(), user.getId())){
                return new ResponseObject(false, "Listing " + request.listingId() + " is already bookmarked");
            }
            Bookmark toAdd = new Bookmark(listing.get(), user.getId());
            bookmarkRepo.save(toAdd);
            return new ResponseObject(true, "Listing " + request.listingId() + " bookmarked");
        }
        else{
            return new ResponseObject(false, "No such listing");
        }
    }

    @PostMapping("/unbookmark")
    public ResponseObject removeBookmark(@RequestBody ListingIdRequest request){
        WayfareUserDetails user = getCurrentUserDetails();

        if (bookmarkRepo.existsByListingIdAndUserId(request.listingId(), user.getId())){
            Bookmark toDelete = bookmarkRepo.findByListingIdAndUserId(request.listingId(), user.getId());
            bookmarkRepo.delete(toDelete);
            return new ResponseObject(true, "Unbookmarked listing " + request.listingId());
        }
        return new ResponseObject(false, "Listing " + request.listingId() + " is not bookmarked.");
    }
}

