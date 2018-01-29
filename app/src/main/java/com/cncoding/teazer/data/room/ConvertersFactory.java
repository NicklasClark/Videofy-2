package com.cncoding.teazer.data.room;

import android.arch.persistence.room.TypeConverter;

import com.cncoding.teazer.data.model.react.ReactionDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.cncoding.teazer.data.model.base.*;
import com.cncoding.teazer.data.model.post.ReactedUser;

import java.util.ArrayList;

/**
 * 
 * Created by Prem$ on 1/22/2018.
 */

public class ConvertersFactory {

//    @TypeConverter
//    public static MiniProfile miniProfileFromString(String value) {
//        return new Gson().fromJson(value, new TypeToken<MiniProfile>() {}.getType());
//    }
//
//    @TypeConverter
//    public static String stringFromMiniProfile(MiniProfile miniProfile) {
//        return new Gson().toJson(miniProfile, new TypeToken<MiniProfile>() {}.getType());
//    }
//
//    @TypeConverter
//    public static CheckIn checkInFromString(String value) {
//        return new Gson().fromJson(value, new TypeToken<CheckIn>() {}.getType());
//    }
//
//    @TypeConverter
//    public static String stringFromCheckIn(CheckIn checkIn) {
//        return new Gson().toJson(checkIn, new TypeToken<CheckIn>() {}.getType());
//    }

    @TypeConverter
    public static ArrayList<Medias> mediasListFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<Medias>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromMediasList(ArrayList<Medias> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<Medias>>() {}.getType());
    }

    @TypeConverter
    public static ArrayList<ReactionDetails> reactionsListFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<ReactionDetails>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromReactionsList(ArrayList<ReactionDetails> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<ReactionDetails>>() {}.getType());
    }

    @TypeConverter
    public static ArrayList<TaggedUser> taggedUsersFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<TaggedUser>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromTaggedUsers(ArrayList<TaggedUser> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<TaggedUser>>() {}.getType());
    }

    @TypeConverter
    public static ArrayList<ReactedUser> reactedUsersFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<ReactedUser>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromReactedUsers(ArrayList<ReactedUser> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<ReactedUser>>() {}.getType());
    }

    @TypeConverter
    public static ArrayList<Category> categoriesFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<Category>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromCategories(ArrayList<Category> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<Category>>() {}.getType());
    }
}
