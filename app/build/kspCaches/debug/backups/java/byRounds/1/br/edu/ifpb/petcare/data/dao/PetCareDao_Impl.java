package br.edu.ifpb.petcare.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import br.edu.ifpb.petcare.data.model.Appointment;
import br.edu.ifpb.petcare.data.model.HealthRecord;
import br.edu.ifpb.petcare.data.model.Pet;
import br.edu.ifpb.petcare.data.model.PetDocument;
import br.edu.ifpb.petcare.data.model.PetProduct;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PetCareDao_Impl implements PetCareDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Pet> __insertionAdapterOfPet;

  private final EntityInsertionAdapter<Appointment> __insertionAdapterOfAppointment;

  private final EntityInsertionAdapter<HealthRecord> __insertionAdapterOfHealthRecord;

  private final EntityInsertionAdapter<PetDocument> __insertionAdapterOfPetDocument;

  private final EntityInsertionAdapter<PetProduct> __insertionAdapterOfPetProduct;

  private final EntityDeletionOrUpdateAdapter<Pet> __deletionAdapterOfPet;

  private final EntityDeletionOrUpdateAdapter<Appointment> __deletionAdapterOfAppointment;

  private final EntityDeletionOrUpdateAdapter<HealthRecord> __deletionAdapterOfHealthRecord;

  private final EntityDeletionOrUpdateAdapter<PetDocument> __deletionAdapterOfPetDocument;

  private final EntityDeletionOrUpdateAdapter<PetProduct> __deletionAdapterOfPetProduct;

  private final EntityDeletionOrUpdateAdapter<Pet> __updateAdapterOfPet;

  private final EntityDeletionOrUpdateAdapter<Appointment> __updateAdapterOfAppointment;

  private final EntityDeletionOrUpdateAdapter<PetProduct> __updateAdapterOfPetProduct;

  public PetCareDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPet = new EntityInsertionAdapter<Pet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pets` (`id`,`name`,`species`,`breed`,`age`,`birthDate`,`description`,`photoUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSpecies());
        statement.bindString(4, entity.getBreed());
        statement.bindString(5, entity.getAge());
        statement.bindString(6, entity.getBirthDate());
        statement.bindString(7, entity.getDescription());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPhotoUri());
        }
      }
    };
    this.__insertionAdapterOfAppointment = new EntityInsertionAdapter<Appointment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `appointments` (`id`,`petId`,`title`,`category`,`date`,`time`,`locationOrVet`,`notes`,`isDone`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Appointment entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPetId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getDate());
        statement.bindString(6, entity.getTime());
        statement.bindString(7, entity.getLocationOrVet());
        statement.bindString(8, entity.getNotes());
        final int _tmp = entity.isDone() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__insertionAdapterOfHealthRecord = new EntityInsertionAdapter<HealthRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medications_vaccines` (`id`,`petId`,`type`,`name`,`dosageOrBatch`,`applicationDate`,`nextDueDate`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPetId());
        statement.bindString(3, entity.getType());
        statement.bindString(4, entity.getName());
        statement.bindString(5, entity.getDosageOrBatch());
        statement.bindString(6, entity.getApplicationDate());
        statement.bindString(7, entity.getNextDueDate());
        statement.bindString(8, entity.getNotes());
      }
    };
    this.__insertionAdapterOfPetDocument = new EntityInsertionAdapter<PetDocument>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `documents` (`id`,`petId`,`title`,`docType`,`issueDate`,`documentNumberOrCode`,`summary`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PetDocument entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPetId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getDocType());
        statement.bindString(5, entity.getIssueDate());
        statement.bindString(6, entity.getDocumentNumberOrCode());
        statement.bindString(7, entity.getSummary());
      }
    };
    this.__insertionAdapterOfPetProduct = new EntityInsertionAdapter<PetProduct>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `products` (`id`,`petId`,`name`,`category`,`price`,`quantity`,`needsRestock`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PetProduct entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPetId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getPetId());
        }
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getCategory());
        statement.bindDouble(5, entity.getPrice());
        statement.bindString(6, entity.getQuantity());
        final int _tmp = entity.getNeedsRestock() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__deletionAdapterOfPet = new EntityDeletionOrUpdateAdapter<Pet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pet entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfAppointment = new EntityDeletionOrUpdateAdapter<Appointment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `appointments` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Appointment entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfHealthRecord = new EntityDeletionOrUpdateAdapter<HealthRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `medications_vaccines` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthRecord entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfPetDocument = new EntityDeletionOrUpdateAdapter<PetDocument>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `documents` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PetDocument entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfPetProduct = new EntityDeletionOrUpdateAdapter<PetProduct>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `products` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PetProduct entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPet = new EntityDeletionOrUpdateAdapter<Pet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pets` SET `id` = ?,`name` = ?,`species` = ?,`breed` = ?,`age` = ?,`birthDate` = ?,`description` = ?,`photoUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSpecies());
        statement.bindString(4, entity.getBreed());
        statement.bindString(5, entity.getAge());
        statement.bindString(6, entity.getBirthDate());
        statement.bindString(7, entity.getDescription());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPhotoUri());
        }
        statement.bindLong(9, entity.getId());
      }
    };
    this.__updateAdapterOfAppointment = new EntityDeletionOrUpdateAdapter<Appointment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `appointments` SET `id` = ?,`petId` = ?,`title` = ?,`category` = ?,`date` = ?,`time` = ?,`locationOrVet` = ?,`notes` = ?,`isDone` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Appointment entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPetId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getDate());
        statement.bindString(6, entity.getTime());
        statement.bindString(7, entity.getLocationOrVet());
        statement.bindString(8, entity.getNotes());
        final int _tmp = entity.isDone() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getId());
      }
    };
    this.__updateAdapterOfPetProduct = new EntityDeletionOrUpdateAdapter<PetProduct>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `products` SET `id` = ?,`petId` = ?,`name` = ?,`category` = ?,`price` = ?,`quantity` = ?,`needsRestock` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PetProduct entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPetId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getPetId());
        }
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getCategory());
        statement.bindDouble(5, entity.getPrice());
        statement.bindString(6, entity.getQuantity());
        final int _tmp = entity.getNeedsRestock() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertPet(final Pet pet, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPet.insertAndReturnId(pet);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAppointment(final Appointment appointment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppointment.insert(appointment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertHealthRecord(final HealthRecord record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHealthRecord.insert(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDocument(final PetDocument document,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPetDocument.insert(document);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertProduct(final PetProduct product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPetProduct.insert(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePet(final Pet pet, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPet.handle(pet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAppointment(final Appointment appointment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAppointment.handle(appointment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHealthRecord(final HealthRecord record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHealthRecord.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDocument(final PetDocument document,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPetDocument.handle(document);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProduct(final PetProduct product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPetProduct.handle(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePet(final Pet pet, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPet.handle(pet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAppointment(final Appointment appointment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAppointment.handle(appointment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProduct(final PetProduct product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPetProduct.handle(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Pet>> getAllPets() {
    final String _sql = "SELECT * FROM pets ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pets"}, new Callable<List<Pet>>() {
      @Override
      @NonNull
      public List<Pet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpecies = CursorUtil.getColumnIndexOrThrow(_cursor, "species");
          final int _cursorIndexOfBreed = CursorUtil.getColumnIndexOrThrow(_cursor, "breed");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfBirthDate = CursorUtil.getColumnIndexOrThrow(_cursor, "birthDate");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final List<Pet> _result = new ArrayList<Pet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pet _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSpecies;
            _tmpSpecies = _cursor.getString(_cursorIndexOfSpecies);
            final String _tmpBreed;
            _tmpBreed = _cursor.getString(_cursorIndexOfBreed);
            final String _tmpAge;
            _tmpAge = _cursor.getString(_cursorIndexOfAge);
            final String _tmpBirthDate;
            _tmpBirthDate = _cursor.getString(_cursorIndexOfBirthDate);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item = new Pet(_tmpId,_tmpName,_tmpSpecies,_tmpBreed,_tmpAge,_tmpBirthDate,_tmpDescription,_tmpPhotoUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPetById(final int id, final Continuation<? super Pet> $completion) {
    final String _sql = "SELECT * FROM pets WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Pet>() {
      @Override
      @Nullable
      public Pet call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSpecies = CursorUtil.getColumnIndexOrThrow(_cursor, "species");
          final int _cursorIndexOfBreed = CursorUtil.getColumnIndexOrThrow(_cursor, "breed");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfBirthDate = CursorUtil.getColumnIndexOrThrow(_cursor, "birthDate");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final Pet _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSpecies;
            _tmpSpecies = _cursor.getString(_cursorIndexOfSpecies);
            final String _tmpBreed;
            _tmpBreed = _cursor.getString(_cursorIndexOfBreed);
            final String _tmpAge;
            _tmpAge = _cursor.getString(_cursorIndexOfAge);
            final String _tmpBirthDate;
            _tmpBirthDate = _cursor.getString(_cursorIndexOfBirthDate);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _result = new Pet(_tmpId,_tmpName,_tmpSpecies,_tmpBreed,_tmpAge,_tmpBirthDate,_tmpDescription,_tmpPhotoUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Appointment>> getAllAppointments() {
    final String _sql = "SELECT * FROM appointments ORDER BY isDone ASC, date ASC, time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"appointments"}, new Callable<List<Appointment>>() {
      @Override
      @NonNull
      public List<Appointment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPetId = CursorUtil.getColumnIndexOrThrow(_cursor, "petId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfLocationOrVet = CursorUtil.getColumnIndexOrThrow(_cursor, "locationOrVet");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isDone");
          final List<Appointment> _result = new ArrayList<Appointment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Appointment _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpPetId;
            _tmpPetId = _cursor.getInt(_cursorIndexOfPetId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpLocationOrVet;
            _tmpLocationOrVet = _cursor.getString(_cursorIndexOfLocationOrVet);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsDone;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDone);
            _tmpIsDone = _tmp != 0;
            _item = new Appointment(_tmpId,_tmpPetId,_tmpTitle,_tmpCategory,_tmpDate,_tmpTime,_tmpLocationOrVet,_tmpNotes,_tmpIsDone);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<HealthRecord>> getAllHealthRecords() {
    final String _sql = "SELECT * FROM medications_vaccines ORDER BY applicationDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medications_vaccines"}, new Callable<List<HealthRecord>>() {
      @Override
      @NonNull
      public List<HealthRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPetId = CursorUtil.getColumnIndexOrThrow(_cursor, "petId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosageOrBatch = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageOrBatch");
          final int _cursorIndexOfApplicationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "applicationDate");
          final int _cursorIndexOfNextDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextDueDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<HealthRecord> _result = new ArrayList<HealthRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthRecord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpPetId;
            _tmpPetId = _cursor.getInt(_cursorIndexOfPetId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDosageOrBatch;
            _tmpDosageOrBatch = _cursor.getString(_cursorIndexOfDosageOrBatch);
            final String _tmpApplicationDate;
            _tmpApplicationDate = _cursor.getString(_cursorIndexOfApplicationDate);
            final String _tmpNextDueDate;
            _tmpNextDueDate = _cursor.getString(_cursorIndexOfNextDueDate);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new HealthRecord(_tmpId,_tmpPetId,_tmpType,_tmpName,_tmpDosageOrBatch,_tmpApplicationDate,_tmpNextDueDate,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PetDocument>> getAllDocuments() {
    final String _sql = "SELECT * FROM documents ORDER BY issueDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"documents"}, new Callable<List<PetDocument>>() {
      @Override
      @NonNull
      public List<PetDocument> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPetId = CursorUtil.getColumnIndexOrThrow(_cursor, "petId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDocType = CursorUtil.getColumnIndexOrThrow(_cursor, "docType");
          final int _cursorIndexOfIssueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "issueDate");
          final int _cursorIndexOfDocumentNumberOrCode = CursorUtil.getColumnIndexOrThrow(_cursor, "documentNumberOrCode");
          final int _cursorIndexOfSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "summary");
          final List<PetDocument> _result = new ArrayList<PetDocument>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PetDocument _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpPetId;
            _tmpPetId = _cursor.getInt(_cursorIndexOfPetId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDocType;
            _tmpDocType = _cursor.getString(_cursorIndexOfDocType);
            final String _tmpIssueDate;
            _tmpIssueDate = _cursor.getString(_cursorIndexOfIssueDate);
            final String _tmpDocumentNumberOrCode;
            _tmpDocumentNumberOrCode = _cursor.getString(_cursorIndexOfDocumentNumberOrCode);
            final String _tmpSummary;
            _tmpSummary = _cursor.getString(_cursorIndexOfSummary);
            _item = new PetDocument(_tmpId,_tmpPetId,_tmpTitle,_tmpDocType,_tmpIssueDate,_tmpDocumentNumberOrCode,_tmpSummary);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PetProduct>> getAllProducts() {
    final String _sql = "SELECT * FROM products ORDER BY needsRestock DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<PetProduct>>() {
      @Override
      @NonNull
      public List<PetProduct> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPetId = CursorUtil.getColumnIndexOrThrow(_cursor, "petId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfNeedsRestock = CursorUtil.getColumnIndexOrThrow(_cursor, "needsRestock");
          final List<PetProduct> _result = new ArrayList<PetProduct>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PetProduct _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpPetId;
            if (_cursor.isNull(_cursorIndexOfPetId)) {
              _tmpPetId = null;
            } else {
              _tmpPetId = _cursor.getInt(_cursorIndexOfPetId);
            }
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final String _tmpQuantity;
            _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
            final boolean _tmpNeedsRestock;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNeedsRestock);
            _tmpNeedsRestock = _tmp != 0;
            _item = new PetProduct(_tmpId,_tmpPetId,_tmpName,_tmpCategory,_tmpPrice,_tmpQuantity,_tmpNeedsRestock);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
