package com.evilcorp.hcf.functions;

import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.query.ReturnableType;
import org.hibernate.query.sqm.sql.internal.SqmParameterInterpretation;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.QueryLiteral;
import org.hibernate.type.StandardBasicTypes;

import java.util.List;

public class LtIntervalFunction extends StandardSQLFunction {

    public LtIntervalFunction(String name) {
        super(name, true, StandardBasicTypes.BOOLEAN);
    }

    @Override
    public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> sqlAstArguments, ReturnableType<?> returnType, SqlAstTranslator<?> translator) {
        sqlAppender.append("(");
        sqlAstArguments.get(0).accept(translator);
        sqlAppender.append(" < ");
        final SqlAstNode pSecond = sqlAstArguments.get(1);
        if (pSecond instanceof QueryLiteral literalNode) {
            // If second parameter is a literal we suppose it's a postgresql literal
            // without "interval" part and without single quotes.
            // Something like the following - "1 year"
            // So we add interval and single quotes to make it "interval '1 year'"
            sqlAppender.append(" interval ");
            sqlAppender.append("'" + literalNode.getLiteralValue().toString() + "'");
        } else if (pSecond instanceof SqmParameterInterpretation literalNode) {
            // If second parameter is an SQL parameter, we suppose it's number of
            // seconds (java type - Long)
            // So we should transform number of seconds to interval using make_interval
            // ? to make_interval(0, 0, 0, 0, 0, 0, ?)
            sqlAppender.append("make_interval(0, 0, 0, 0, 0, 0, ");
            pSecond.accept(translator);
            sqlAppender.append(")");
        } else {
            pSecond.accept(translator);
        }
        sqlAppender.append(")");
    }
}
