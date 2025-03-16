package com.example.f_food.DAO;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantRoomDatabase_Impl extends RestaurantRoomDatabase {
  private volatile RestaurantDAO _restaurantDAO;

  private volatile FoodDAO _foodDAO;

  private volatile PolicyDAO _policyDAO;

  private volatile UserDAO _userDAO;

  private volatile OrderDAO _orderDAO;

  private volatile ShipperDAO _shipperDAO;

  private volatile PaymentDAO _paymentDAO;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(7) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Restaurants` (`restaurant_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `name` TEXT, `address` TEXT, `phone` TEXT, `status` TEXT, `created_at` TEXT, `image` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Foods` (`food_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `restaurant_id` INTEGER NOT NULL, `name` TEXT, `description` TEXT, `price` REAL NOT NULL, `category` TEXT, `image_url` TEXT, `stock_status` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Users` (`UserID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `FullName` TEXT, `Email` TEXT, `Password` TEXT, `Phone` TEXT, `UserType` TEXT, `CreatedAt` TEXT, `UpdatedAt` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Policies` (`policy_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `description` TEXT, `created_at` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Orders` (`order_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `restaurant_id` INTEGER NOT NULL, `total_price` REAL NOT NULL, `payment_method` TEXT, `order_status` TEXT, `created_at` TEXT, `updated_at` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Shippers` (`ShipperID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `UserID` INTEGER NOT NULL, `Status` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Payments` (`payment_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `order_id` INTEGER NOT NULL, `amount` REAL NOT NULL, `payment_method` TEXT, `payment_status` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8243f74470c32240611e353c902478e5')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `Restaurants`");
        db.execSQL("DROP TABLE IF EXISTS `Foods`");
        db.execSQL("DROP TABLE IF EXISTS `Users`");
        db.execSQL("DROP TABLE IF EXISTS `Policies`");
        db.execSQL("DROP TABLE IF EXISTS `Orders`");
        db.execSQL("DROP TABLE IF EXISTS `Shippers`");
        db.execSQL("DROP TABLE IF EXISTS `Payments`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRestaurants = new HashMap<String, TableInfo.Column>(8);
        _columnsRestaurants.put("restaurant_id", new TableInfo.Column("restaurant_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("image", new TableInfo.Column("image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRestaurants = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRestaurants = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRestaurants = new TableInfo("Restaurants", _columnsRestaurants, _foreignKeysRestaurants, _indicesRestaurants);
        final TableInfo _existingRestaurants = TableInfo.read(db, "Restaurants");
        if (!_infoRestaurants.equals(_existingRestaurants)) {
          return new RoomOpenHelper.ValidationResult(false, "Restaurants(com.example.f_food.Entity.Restaurant).\n"
                  + " Expected:\n" + _infoRestaurants + "\n"
                  + " Found:\n" + _existingRestaurants);
        }
        final HashMap<String, TableInfo.Column> _columnsFoods = new HashMap<String, TableInfo.Column>(8);
        _columnsFoods.put("food_id", new TableInfo.Column("food_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("restaurant_id", new TableInfo.Column("restaurant_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("price", new TableInfo.Column("price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("image_url", new TableInfo.Column("image_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoods.put("stock_status", new TableInfo.Column("stock_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFoods = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFoods = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFoods = new TableInfo("Foods", _columnsFoods, _foreignKeysFoods, _indicesFoods);
        final TableInfo _existingFoods = TableInfo.read(db, "Foods");
        if (!_infoFoods.equals(_existingFoods)) {
          return new RoomOpenHelper.ValidationResult(false, "Foods(com.example.f_food.Entity.Food).\n"
                  + " Expected:\n" + _infoFoods + "\n"
                  + " Found:\n" + _existingFoods);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(8);
        _columnsUsers.put("UserID", new TableInfo.Column("UserID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("FullName", new TableInfo.Column("FullName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("Email", new TableInfo.Column("Email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("Password", new TableInfo.Column("Password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("Phone", new TableInfo.Column("Phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("UserType", new TableInfo.Column("UserType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("CreatedAt", new TableInfo.Column("CreatedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("UpdatedAt", new TableInfo.Column("UpdatedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("Users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "Users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "Users(com.example.f_food.Entity.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsPolicies = new HashMap<String, TableInfo.Column>(4);
        _columnsPolicies.put("policy_id", new TableInfo.Column("policy_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPolicies.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPolicies.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPolicies.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPolicies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPolicies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPolicies = new TableInfo("Policies", _columnsPolicies, _foreignKeysPolicies, _indicesPolicies);
        final TableInfo _existingPolicies = TableInfo.read(db, "Policies");
        if (!_infoPolicies.equals(_existingPolicies)) {
          return new RoomOpenHelper.ValidationResult(false, "Policies(com.example.f_food.Entity.Policy).\n"
                  + " Expected:\n" + _infoPolicies + "\n"
                  + " Found:\n" + _existingPolicies);
        }
        final HashMap<String, TableInfo.Column> _columnsOrders = new HashMap<String, TableInfo.Column>(8);
        _columnsOrders.put("order_id", new TableInfo.Column("order_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("restaurant_id", new TableInfo.Column("restaurant_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("total_price", new TableInfo.Column("total_price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("payment_method", new TableInfo.Column("payment_method", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("order_status", new TableInfo.Column("order_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("created_at", new TableInfo.Column("created_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("updated_at", new TableInfo.Column("updated_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOrders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOrders = new TableInfo("Orders", _columnsOrders, _foreignKeysOrders, _indicesOrders);
        final TableInfo _existingOrders = TableInfo.read(db, "Orders");
        if (!_infoOrders.equals(_existingOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "Orders(com.example.f_food.Entity.Order).\n"
                  + " Expected:\n" + _infoOrders + "\n"
                  + " Found:\n" + _existingOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsShippers = new HashMap<String, TableInfo.Column>(3);
        _columnsShippers.put("ShipperID", new TableInfo.Column("ShipperID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShippers.put("UserID", new TableInfo.Column("UserID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsShippers.put("Status", new TableInfo.Column("Status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysShippers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesShippers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoShippers = new TableInfo("Shippers", _columnsShippers, _foreignKeysShippers, _indicesShippers);
        final TableInfo _existingShippers = TableInfo.read(db, "Shippers");
        if (!_infoShippers.equals(_existingShippers)) {
          return new RoomOpenHelper.ValidationResult(false, "Shippers(com.example.f_food.Entity.Shipper).\n"
                  + " Expected:\n" + _infoShippers + "\n"
                  + " Found:\n" + _existingShippers);
        }
        final HashMap<String, TableInfo.Column> _columnsPayments = new HashMap<String, TableInfo.Column>(5);
        _columnsPayments.put("payment_id", new TableInfo.Column("payment_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("order_id", new TableInfo.Column("order_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("payment_method", new TableInfo.Column("payment_method", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("payment_status", new TableInfo.Column("payment_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPayments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPayments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPayments = new TableInfo("Payments", _columnsPayments, _foreignKeysPayments, _indicesPayments);
        final TableInfo _existingPayments = TableInfo.read(db, "Payments");
        if (!_infoPayments.equals(_existingPayments)) {
          return new RoomOpenHelper.ValidationResult(false, "Payments(com.example.f_food.Entity.Payment).\n"
                  + " Expected:\n" + _infoPayments + "\n"
                  + " Found:\n" + _existingPayments);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8243f74470c32240611e353c902478e5", "1c46a4f86387b0fe4fda5433ccda2752");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Restaurants","Foods","Users","Policies","Orders","Shippers","Payments");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Restaurants`");
      _db.execSQL("DELETE FROM `Foods`");
      _db.execSQL("DELETE FROM `Users`");
      _db.execSQL("DELETE FROM `Policies`");
      _db.execSQL("DELETE FROM `Orders`");
      _db.execSQL("DELETE FROM `Shippers`");
      _db.execSQL("DELETE FROM `Payments`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RestaurantDAO.class, RestaurantDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(FoodDAO.class, FoodDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(PolicyDAO.class, PolicyDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDAO.class, UserDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(OrderDAO.class, OrderDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(ShipperDAO.class, ShipperDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(PaymentDAO.class, PaymentDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RestaurantDAO restaurantDAO() {
    if (_restaurantDAO != null) {
      return _restaurantDAO;
    } else {
      synchronized(this) {
        if(_restaurantDAO == null) {
          _restaurantDAO = new RestaurantDAO_Impl(this);
        }
        return _restaurantDAO;
      }
    }
  }

  @Override
  public FoodDAO foodDAO() {
    if (_foodDAO != null) {
      return _foodDAO;
    } else {
      synchronized(this) {
        if(_foodDAO == null) {
          _foodDAO = new FoodDAO_Impl(this);
        }
        return _foodDAO;
      }
    }
  }

  @Override
  public PolicyDAO policyDAO() {
    if (_policyDAO != null) {
      return _policyDAO;
    } else {
      synchronized(this) {
        if(_policyDAO == null) {
          _policyDAO = new PolicyDAO_Impl(this);
        }
        return _policyDAO;
      }
    }
  }

  @Override
  public UserDAO userDAO() {
    if (_userDAO != null) {
      return _userDAO;
    } else {
      synchronized(this) {
        if(_userDAO == null) {
          _userDAO = new UserDAO_Impl(this);
        }
        return _userDAO;
      }
    }
  }

  @Override
  public OrderDAO orderDAO() {
    if (_orderDAO != null) {
      return _orderDAO;
    } else {
      synchronized(this) {
        if(_orderDAO == null) {
          _orderDAO = new OrderDAO_Impl(this);
        }
        return _orderDAO;
      }
    }
  }

  @Override
  public ShipperDAO shipperDAO() {
    if (_shipperDAO != null) {
      return _shipperDAO;
    } else {
      synchronized(this) {
        if(_shipperDAO == null) {
          _shipperDAO = new ShipperDAO_Impl(this);
        }
        return _shipperDAO;
      }
    }
  }

  @Override
  public PaymentDAO paymentDAO() {
    if (_paymentDAO != null) {
      return _paymentDAO;
    } else {
      synchronized(this) {
        if(_paymentDAO == null) {
          _paymentDAO = new PaymentDAO_Impl(this);
        }
        return _paymentDAO;
      }
    }
  }
}
