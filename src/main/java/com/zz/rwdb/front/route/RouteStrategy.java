package com.zz.rwdb.front.route;

import java.sql.SQLSyntaxErrorException;

public interface RouteStrategy {

	String route(RouteCondition condition) throws SQLSyntaxErrorException;
}