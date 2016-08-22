CREATE OR REPLACE procedure p_sales_daily_achievement(year_month in VARCHAR2)
as
   v_baseline_timestamp SK_Sales_DAY_ACHIEVEMENT.baseline_timestamp%TYPE;
   month_achievement_rate    sk_sales_day_achievement.month_achievement_rate%type;
   TYPE SelectCursorRef IS REF CURSOR;
   selectCursor SelectCursorRef;
   sapid SK_Sales_DAY_ACHIEVEMENT.sapid%TYPE;
   invoice_timestamp SK_Sales_DAY_ACHIEVEMENT.baseline_timestamp%TYPE;
   amount SK_Sales_DAY_ACHIEVEMENT.sales_amount%TYPE;
   tax number;
   cost SK_Sales_DAY_ACHIEVEMENT.cost%TYPE;
   premium_discount SK_Sales_DAY_ACHIEVEMENT.premium_discount%TYPE;
   premium_discount_tax number;
   sales_discount SK_Sales_DAY_ACHIEVEMENT.sales_discount%TYPE;
   sales_discount_tax number;
   sales_return SK_Sales_DAY_ACHIEVEMENT.sales_return%TYPE;
   gross_profit_rate SK_Sales_DAY_ACHIEVEMENT.gross_profit_rate%TYPE;
   budget SK_Sales_DAY_ACHIEVEMENT.budget_month%TYPE;
   budget_month SK_Sales_DAY_ACHIEVEMENT.budget_month%TYPE;
   day_achievement_rate SK_Sales_DAY_ACHIEVEMENT.day_achievement_rate%TYPE;
   sales_amount SK_Sales_DAY_ACHIEVEMENT.sales_amount%TYPE;
   return_cost SK_Sales_DAY_ACHIEVEMENT.return_cost%TYPE;
   return_amount SK_Sales_DAY_ACHIEVEMENT.return_amount%TYPE;
   contract_amount SK_Sales_DAY_ACHIEVEMENT.contract_amount%TYPE;
   contract_cost SK_Sales_DAY_ACHIEVEMENT.contract_cost%TYPE;
begin
	--v_baseline_timestamp = to_date( concat(year_month,'01'),'YYYYMMDD');
   --取得每天的銷售資料 year_month=201202
	OPEN selectCursor FOR select sapid,invoice_timestamp,amount,tax,cost 
	,premium_discount,premium_discount_tax,sales_discount,sales_discount_tax,sales_return
	,budget,sales_amount,return_cost,return_amount,contract_amount,contract_cost from
	v_cal_sales_day_achievement where to_char(invoice_timestamp,'YYYYMM')=year_month;
	DELETE FROM SK_SALES_DAY_ACHIEVEMENT WHERE to_char(baseline_timestamp,'YYYYMM')=year_month;
	-- 提取下一筆指標
	LOOP
		Fetch selectCursor Into  sapid,invoice_timestamp,amount,tax,cost 
	,premium_discount,premium_discount_tax,sales_discount,sales_discount_tax,sales_return
	,budget,sales_amount,return_cost,return_amount,contract_amount,contract_cost;
	IF( premium_discount is null ) THEN
		premium_discount := 0;
	END IF;
	IF( premium_discount_tax is null ) THEN
		premium_discount_tax := 0;
	END IF;
	IF( sales_discount is null ) THEN
		sales_discount := 0;
	END IF;
	IF( sales_discount_tax is null ) THEN
		sales_discount_tax := 0;
	END IF;
	IF( sales_return is null ) THEN
		sales_return := 0;
	END IF;
	IF( return_cost is null ) THEN
		return_cost := 0;
	END IF;
	IF( sales_amount is null or sales_amount = 0 ) THEN
		sales_amount := 0;
		gross_profit_rate :=0;
	ELSE
		gross_profit_rate := ((sales_amount -cost + return_cost)/ sales_amount);
	END IF;
	IF( return_amount is null ) THEN
		return_amount := 0;
	END IF;
	IF( budget is null or budget = 0 ) THEN 
		day_achievement_rate := 0;
	ELSE
		day_achievement_rate := sales_amount/budget;
	END IF;
	EXIT WHEN selectCursor%NOTFOUND;
	Insert into SK_SALES_DAY_ACHIEVEMENT(
		ID,BASELINE_TIMESTAMP,SAPID,INVOICE_AMOUNT,PREMIUM_DISCOUNT,
		SALES_DISCOUNT,SALES_RETURN,SALES_AMOUNT,COST,GROSS_PROFIT_RATE,
		BUDGET_MONTH,DAY_ACHIEVEMENT_RATE,MONTH_ACHIEVEMENT_RATE,RETURN_COST,RETURN_AMOUNT,CONTRACT_AMOUNT,CONTRACT_COST
		)values(SEQ_SK_ARCH_D.NEXTVAL,invoice_timestamp, sapid, amount, premium_discount,
		sales_discount,sales_return,sales_amount, cost, gross_profit_rate,
		budget,null,day_achievement_rate,return_cost,return_amount,contract_amount,contract_cost
		);
	END LOOP;
    -- 關閉指標
    Close selectCursor;
    -- 移除釋放指標
    --Deallocate selectCursor;
end p_sales_daily_achievement;