package com.cncoding.teazer.injection.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 *
 * Created by Prem$ on 3/8/2018.
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface ApplicationScope {}