package ke.co.shofcosacco.app.api.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ke.co.shofcosacco.app.api.responses.LoanBalanceResponse;
import ke.co.shofcosacco.app.api.responses.LoanProductsResponse;
import ke.co.shofcosacco.app.api.responses.LoanStatementResponse;
import ke.co.shofcosacco.app.api.responses.SourceAccountResponse;
import ke.co.shofcosacco.app.api.responses.StatementResponse;
import ke.co.shofcosacco.app.models.LoanBalance;
import ke.co.shofcosacco.app.models.LoanProduct;
import ke.co.shofcosacco.app.models.LoanStatement;
import ke.co.shofcosacco.app.models.MiniStatement;
import ke.co.shofcosacco.app.models.SourceAccount;


public class ResponseDeserializer<T> implements JsonDeserializer<T> {

    private final Class<T> responseType;

    public ResponseDeserializer(Class<T> responseType) {
        this.responseType = responseType;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        try {
            T response = responseType.newInstance();

            if (response instanceof LoanBalanceResponse) {
                setFields((LoanBalanceResponse) response, jsonObject, context);
            } else if (response instanceof LoanProductsResponse) {
                setFields((LoanProductsResponse) response, jsonObject, context);
            } else if (response instanceof LoanStatementResponse) {
                setFields((LoanStatementResponse) response, jsonObject, context);
            } else if (response instanceof SourceAccountResponse) {
                setFields((SourceAccountResponse) response, jsonObject, context);
            }else if (response instanceof StatementResponse) {
                setFields((StatementResponse) response, jsonObject, context);
            }

            return response;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to create response object.", e);
        }
    }

    private void setFields(LoanBalanceResponse response, JsonObject jsonObject, JsonDeserializationContext context) {
        response.statusCode = jsonObject.get("success").getAsString();
        response.statusDesc = jsonObject.get("description").getAsString();

        JsonElement loanProductsElement = jsonObject.get("loan_list");

        if (loanProductsElement.isJsonArray()) {
            response.loanBalances = context.deserialize(loanProductsElement, new TypeToken<List<LoanBalance>>() {}.getType());
        } else if (loanProductsElement.isJsonObject()) {
            LoanBalance singleProduct = context.deserialize(loanProductsElement, LoanBalance.class);
            response.loanBalances = new ArrayList<>();
            response.loanBalances.add(singleProduct);
        } else {
            response.loanBalances = null;
        }
    }

    private void setFields(LoanProductsResponse response, JsonObject jsonObject, JsonDeserializationContext context) {
        response.statusCode = jsonObject.get("success").getAsString();
        response.statusDesc = jsonObject.get("description").getAsString();

        JsonElement loanProductsElement = jsonObject.get("loan_products");

        if (loanProductsElement.isJsonArray()) {
            response.loanProducts = context.deserialize(loanProductsElement, new TypeToken<List<LoanProduct>>() {}.getType());
        } else if (loanProductsElement.isJsonObject()) {
            LoanProduct singleProduct = context.deserialize(loanProductsElement, LoanProduct.class);
            response.loanProducts = new ArrayList<>();
            response.loanProducts.add(singleProduct);
        } else {
            response.loanProducts = null;
        }
    }
    private void setFields(LoanStatementResponse response, JsonObject jsonObject, JsonDeserializationContext context) {
        response.statusCode = jsonObject.get("success").getAsString();
        response.statusDesc = jsonObject.get("description").getAsString();

        JsonElement loanProductsElement = jsonObject.get("loans_stm_list");

        if (loanProductsElement.isJsonArray()) {
            response.loanStatementList = context.deserialize(loanProductsElement, new TypeToken<List<LoanStatement>>() {}.getType());
        } else if (loanProductsElement.isJsonObject()) {
            LoanStatement singleProduct = context.deserialize(loanProductsElement, LoanStatement.class);
            response.loanStatementList = new ArrayList<>();
            response.loanStatementList.add(singleProduct);
        } else {
            response.loanStatementList = null;
        }
    }

    private void setFields(SourceAccountResponse response, JsonObject jsonObject, JsonDeserializationContext context) {
        response.statusCode = jsonObject.get("success").getAsString();
        response.statusDesc = jsonObject.get("description").getAsString();

        JsonElement loanProductsElement = jsonObject.get("source_account_fosa");

        if (loanProductsElement.isJsonArray()) {
            response.sourceAccounts = context.deserialize(loanProductsElement, new TypeToken<List<SourceAccount>>() {}.getType());
        } else if (loanProductsElement.isJsonObject()) {
            SourceAccount singleProduct = context.deserialize(loanProductsElement, SourceAccount.class);
            response.sourceAccounts = new ArrayList<>();
            response.sourceAccounts.add(singleProduct);
        } else {
            response.sourceAccounts = null;
        }
    }

    private void setFields(StatementResponse response, JsonObject jsonObject, JsonDeserializationContext context) {
        response.statusCode = jsonObject.get("success").getAsString();
        response.statusDesc = jsonObject.get("description").getAsString();

        JsonElement loanProductsElement = jsonObject.get("stm_list");

        if (loanProductsElement.isJsonArray()) {
            response.miniStatement = context.deserialize(loanProductsElement, new TypeToken<List<MiniStatement>>() {}.getType());
        } else if (loanProductsElement.isJsonObject()) {
            MiniStatement singleProduct = context.deserialize(loanProductsElement, MiniStatement.class);
            response.miniStatement = new ArrayList<>();
            response.miniStatement.add(singleProduct);
        } else {
            response.miniStatement = null;
        }
    }


}