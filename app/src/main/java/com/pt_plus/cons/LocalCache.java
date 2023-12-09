package com.pt_plus.cons;

import com.pt_plus.Model.AddressModel;
import com.pt_plus.Model.BookingModel;
import com.pt_plus.Model.CenterFilterModel;
import com.pt_plus.Model.OrderModel;
import com.pt_plus.Model.ProductFilterModel;
import com.pt_plus.Model.ProductModel;
import com.pt_plus.Model.TrainerFilterModel;

import org.json.JSONArray;

import java.util.Map;

public class LocalCache {

    private LocalCache() {
    }

    private static LocalCache _self;

    private static Map<String, JSONArray> keyItemMap = null;

    public static LocalCache getCache() {
        if (_self == null) {
            _self = new LocalCache();
        }
        return _self;
    }

    private OrderModel selectedOrder = null;

    public void setSelectedOrder(OrderModel orderModel) {
        if (orderModel != null) {
            selectedOrder = orderModel;
        }

    }

    public OrderModel getSelectedOrder() {
        if (selectedOrder != null) {
            return selectedOrder;
        }
        return null;
    }

    public OrderModel clearSelectedOrder() {
        if (selectedOrder != null) {
            return selectedOrder = null;
        }
        return null;
    }

    private BookingModel selectedBookingModel = new BookingModel();

    public void setSelectedBookingModel(BookingModel bookingModel) {
        if (bookingModel != null) {
            selectedBookingModel = bookingModel;
        }

    }

    public BookingModel getSelectedBookingModel() {
        if (selectedBookingModel != null) {
            return selectedBookingModel;
        }
        return null;
    }

    public BookingModel clearSelectedBookingModel() {
        if (selectedBookingModel != null) {
            return selectedBookingModel = null;
        }
        return null;
    }

    private TrainerFilterModel trainerFilterModel = new TrainerFilterModel();

    public void setTrainerFilterModel(TrainerFilterModel trainerFilterModel) {
        if (trainerFilterModel != null) {
            trainerFilterModel = trainerFilterModel;
        }

    }

    public TrainerFilterModel getTrainerFilterModel() {
        if (trainerFilterModel != null) {
            return trainerFilterModel;
        }
        return null;
    }

    public TrainerFilterModel clearTrainerFilter() {
        if (trainerFilterModel != null) {
            return trainerFilterModel = null;
        }
        return null;
    }



    private CenterFilterModel centerFilterModel = new CenterFilterModel();

    public void setCenterFilterModel(CenterFilterModel centerFilterModel) {
        if (centerFilterModel != null) {
            centerFilterModel = centerFilterModel;
        }

    }

    public CenterFilterModel getCenterFilterModel() {
        if (centerFilterModel != null) {
            return centerFilterModel;
        }
        return null;
    }

    public CenterFilterModel clearCenterFilterModel() {
        if (centerFilterModel != null) {
            return centerFilterModel = null;
        }
        return null;
    }


//     product filter modal
private ProductFilterModel productFilterModel = new ProductFilterModel();



    public ProductFilterModel getProductFilterModel() {
        if (productFilterModel != null) {
            return productFilterModel;
        }
        return null;
    }

    public ProductFilterModel clearProductFilter() {
        if (productFilterModel != null) {
            return productFilterModel = null;
        }
        return null;
    }
}
