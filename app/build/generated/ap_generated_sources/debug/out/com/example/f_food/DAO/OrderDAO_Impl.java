package com.example.f_food.DAO;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.f_food.Entity.Order;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OrderDAO_Impl implements OrderDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Order> __insertionAdapterOfOrder;

  private final EntityDeletionOrUpdateAdapter<Order> __deletionAdapterOfOrder;

  private final EntityDeletionOrUpdateAdapter<Order> __updateAdapterOfOrder;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public OrderDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrder = new EntityInsertionAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `Orders` (`order_id`,`user_id`,`restaurant_id`,`total_price`,`payment_method`,`order_status`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Order entity) {
        statement.bindLong(1, entity.getOrderId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getRestaurantId());
        statement.bindDouble(4, entity.getTotalPrice());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPaymentMethod());
        }
        if (entity.getOrderStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOrderStatus());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCreatedAt());
        }
        if (entity.getUpdatedAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getUpdatedAt());
        }
      }
    };
    this.__deletionAdapterOfOrder = new EntityDeletionOrUpdateAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Orders` WHERE `order_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Order entity) {
        statement.bindLong(1, entity.getOrderId());
      }
    };
    this.__updateAdapterOfOrder = new EntityDeletionOrUpdateAdapter<Order>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Orders` SET `order_id` = ?,`user_id` = ?,`restaurant_id` = ?,`total_price` = ?,`payment_method` = ?,`order_status` = ?,`created_at` = ?,`updated_at` = ? WHERE `order_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Order entity) {
        statement.bindLong(1, entity.getOrderId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getRestaurantId());
        statement.bindDouble(4, entity.getTotalPrice());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPaymentMethod());
        }
        if (entity.getOrderStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOrderStatus());
        }
        if (entity.getCreatedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCreatedAt());
        }
        if (entity.getUpdatedAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getUpdatedAt());
        }
        statement.bindLong(9, entity.getOrderId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Orders WHERE order_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Orders";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Order order) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfOrder.insert(order);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<Order> orders) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfOrder.insert(orders);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Order order) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfOrder.handle(order);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Order order) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfOrder.handle(order);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<Order> getAllOrders() {
    final String _sql = "SELECT * FROM Orders";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "order_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfRestaurantId = CursorUtil.getColumnIndexOrThrow(_cursor, "restaurant_id");
      final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "total_price");
      final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
      final int _cursorIndexOfOrderStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "order_status");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
      final List<Order> _result = new ArrayList<Order>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Order _item;
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpRestaurantId;
        _tmpRestaurantId = _cursor.getInt(_cursorIndexOfRestaurantId);
        final double _tmpTotalPrice;
        _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
        final String _tmpPaymentMethod;
        if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
          _tmpPaymentMethod = null;
        } else {
          _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
        }
        final String _tmpOrderStatus;
        if (_cursor.isNull(_cursorIndexOfOrderStatus)) {
          _tmpOrderStatus = null;
        } else {
          _tmpOrderStatus = _cursor.getString(_cursorIndexOfOrderStatus);
        }
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        final String _tmpUpdatedAt;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmpUpdatedAt = null;
        } else {
          _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
        }
        _item = new Order(_tmpUserId,_tmpRestaurantId,_tmpTotalPrice,_tmpPaymentMethod,_tmpOrderStatus,_tmpCreatedAt,_tmpUpdatedAt);
        final int _tmpOrderId;
        _tmpOrderId = _cursor.getInt(_cursorIndexOfOrderId);
        _item.setOrderId(_tmpOrderId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Order getOrderById(final int id) {
    final String _sql = "SELECT * FROM Orders WHERE order_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "order_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfRestaurantId = CursorUtil.getColumnIndexOrThrow(_cursor, "restaurant_id");
      final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "total_price");
      final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
      final int _cursorIndexOfOrderStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "order_status");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
      final Order _result;
      if (_cursor.moveToFirst()) {
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpRestaurantId;
        _tmpRestaurantId = _cursor.getInt(_cursorIndexOfRestaurantId);
        final double _tmpTotalPrice;
        _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
        final String _tmpPaymentMethod;
        if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
          _tmpPaymentMethod = null;
        } else {
          _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
        }
        final String _tmpOrderStatus;
        if (_cursor.isNull(_cursorIndexOfOrderStatus)) {
          _tmpOrderStatus = null;
        } else {
          _tmpOrderStatus = _cursor.getString(_cursorIndexOfOrderStatus);
        }
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        final String _tmpUpdatedAt;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmpUpdatedAt = null;
        } else {
          _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
        }
        _result = new Order(_tmpUserId,_tmpRestaurantId,_tmpTotalPrice,_tmpPaymentMethod,_tmpOrderStatus,_tmpCreatedAt,_tmpUpdatedAt);
        final int _tmpOrderId;
        _tmpOrderId = _cursor.getInt(_cursorIndexOfOrderId);
        _result.setOrderId(_tmpOrderId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Order> getOrdersByUserId(final int userId) {
    final String _sql = "SELECT * FROM Orders WHERE user_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "order_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfRestaurantId = CursorUtil.getColumnIndexOrThrow(_cursor, "restaurant_id");
      final int _cursorIndexOfTotalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "total_price");
      final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
      final int _cursorIndexOfOrderStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "order_status");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
      final List<Order> _result = new ArrayList<Order>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Order _item;
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpRestaurantId;
        _tmpRestaurantId = _cursor.getInt(_cursorIndexOfRestaurantId);
        final double _tmpTotalPrice;
        _tmpTotalPrice = _cursor.getDouble(_cursorIndexOfTotalPrice);
        final String _tmpPaymentMethod;
        if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
          _tmpPaymentMethod = null;
        } else {
          _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
        }
        final String _tmpOrderStatus;
        if (_cursor.isNull(_cursorIndexOfOrderStatus)) {
          _tmpOrderStatus = null;
        } else {
          _tmpOrderStatus = _cursor.getString(_cursorIndexOfOrderStatus);
        }
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        final String _tmpUpdatedAt;
        if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
          _tmpUpdatedAt = null;
        } else {
          _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
        }
        _item = new Order(_tmpUserId,_tmpRestaurantId,_tmpTotalPrice,_tmpPaymentMethod,_tmpOrderStatus,_tmpCreatedAt,_tmpUpdatedAt);
        final int _tmpOrderId;
        _tmpOrderId = _cursor.getInt(_cursorIndexOfOrderId);
        _item.setOrderId(_tmpOrderId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
