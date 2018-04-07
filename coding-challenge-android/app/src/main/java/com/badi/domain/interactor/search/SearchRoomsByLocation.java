package com.badi.domain.interactor.search;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.data.entity.search.SearchRooms;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.RoomRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving the search room data by location
 */
public class SearchRoomsByLocation extends UseCase {

    private static final int ROOMS_PER_PAGE = 20;

    private final RoomRepository roomRepository;
    private Location location;
    private Integer page, offset;
    private Filters filters;
    private String publishedAtDescendant = "published_at desc";

    @Inject
    public SearchRoomsByLocation(RoomRepository roomRepository, ThreadExecutor threadExecutor, PostExecutionThread
            postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    public void execute(Location location, Filters filters, DisposableObserver useCaseObserver) {
        this.location = location;
        this.filters = filters;
        this.page = 1;
        this.offset = 0;
        super.execute(useCaseObserver);
    }

    public void executePaginated(Integer page, Integer offset, DisposableObserver useCaseObserver) {
        this.page = page;
        this.offset = offset;
        super.execute(useCaseObserver);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        SearchRooms searchRoomsRequest = composeRequest();
        return roomRepository.getRoomsBySearch(searchRoomsRequest);
    }

    private SearchRooms composeRequest() {
        SearchRooms searchRoomsRequest;
        if (filters != null) {
            searchRoomsRequest = SearchRooms.builder()
                    .setLocation(location)
                    .setPage(page)
                    .setPer(ROOMS_PER_PAGE)
                    .setOffset(offset)
                    .setSortBy(publishedAtDescendant)
                    .setNewSearchMode(true)
                    .setMaxPrice(filters.maxPrice())
                    .setAvailableFrom(filters.availableFrom())
                    .setGender(filters.gender())
                    .setNumberOfTenants(filters.numberOfTenants())
                    .setBedTypes(filters.bedTypes())
                    .setAmenities(filters.amenities())
                    .build();
        } else {
            searchRoomsRequest = SearchRooms.builder()
                    .setLocation(location)
                    .setPage(page)
                    .setPer(ROOMS_PER_PAGE)
                    .setOffset(offset)
                    .setSortBy(publishedAtDescendant)
                    .setNewSearchMode(true)
                    .build();
        }

        return searchRoomsRequest;
    }
}
