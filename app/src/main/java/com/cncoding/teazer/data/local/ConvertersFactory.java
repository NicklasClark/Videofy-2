package com.cncoding.teazer.data.local;

import android.arch.persistence.room.TypeConverter;

import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.data.model.base.TaggedUser;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.post.ReactedUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Contract;

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

    @Contract("null -> null")
    @TypeConverter
    public static ArrayList<Medias> mediasListFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<Medias>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromMediasList(ArrayList<Medias> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<Medias>>() {}.getType());
    }

    @Contract("null -> null")
    @TypeConverter
    public static ArrayList<PostReaction> reactionsListFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<PostReaction>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromReactionsList(ArrayList<PostReaction> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<PostReaction>>() {}.getType());
    }

    @Contract("null -> null")
    @TypeConverter
    public static ArrayList<TaggedUser> taggedUsersFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<TaggedUser>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromTaggedUsers(ArrayList<TaggedUser> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<TaggedUser>>() {}.getType());
    }

    @Contract("null -> null")
    @TypeConverter
    public static ArrayList<ReactedUser> reactedUsersFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<ReactedUser>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromReactedUsers(ArrayList<ReactedUser> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<ReactedUser>>() {}.getType());
    }

    @Contract("null -> null")
    @TypeConverter
    public static ArrayList<Category> categoriesFromString(String value) {
        return new Gson().fromJson(value, new TypeToken<ArrayList<Category>>() {}.getType());
    }

    @TypeConverter
    public static String stringFromCategories(ArrayList<Category> list) {
        return new Gson().toJson(list, new TypeToken<ArrayList<Category>>() {}.getType());
    }
}