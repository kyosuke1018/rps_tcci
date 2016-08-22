create or replace
procedure p_sales_month_achievement(year_month in VARCHAR2)
as
   v_baseline_timestamp VARCHAR2(6);
   month_achievement_rate    SK_SALES_MONTH_ACHIEVEMENT.month_achievement_rate%type;
   TYPE SelectCursorRef IS REF CURSOR;
   selectCursor SelectCursorRef;
   sapid SK_SALES_MONTH_ACHIEVEMENT.sapid%TYPE;
   invoice_timestamp SK_SALES_MONTH_ACHIEVEMENT.baseline_timestamp%TYPE;
   amount SK_SALES_MONTH_ACHIEVEMENT.sales_amount%TYPE;
   tax number;
   cost SK_SALES_MONTH_ACHIEVEMENT.cost%TYPE;
   premium_discount SK_SALES_MONTH_ACHIEVEMENT.premium_discount%TYPE;
   premium_discount_tax number;
   sales_discount SK_SALES_MONTH_ACHIEVEMENT.sales_discount%TYPE;
   sales_discount_tax number;
   sales_return SK_SALES_MONTH_ACHIEVEMENT.sales_return%TYPE;
   budget SK_SALES_MONTH_ACHIEVEMENT.budget_month%TYPE;
   sales_amount SK_SALES_MONTH_ACHIEVEMENT.sales_amount%TYPE;
   return_cost SK_SALES_MONTH_ACHIEVEMENT.return_cost%TYPE;
   return_amount SK_SALES_MONTH_ACHIEVEMENT.return_amount%TYPE;
   contract_amount SK_SALES_MONTH_ACHIEVEMENT.contract_amount%TYPE;
   contract_cost SK_SALES_MONTH_ACHIEVEMENT.contract_cost%TYPE;
   payment_rate SK_SALES_MONTH_ACHIEVEMENT.PAYMENT_RATE%TYPE;
   weight SK_SALES_MONTH_ACHIEVEMENT.WEIGHT%TYPE;
   overdue_amount SK_SALES_MONTH_ACHIEVEMENT.OVERDUE_AMOUNT%TYPE;
   gross_profit_rate SK_SALES_MONTH_ACHIEVEMENT.GROSS_PROFIT_RATE%TYPE;
begin
	--v_baseline_timestamp = to_date(year_month,'YYYYMM');
   --取得每天的銷售資料 year_month=201202
	OPEN selectCursor FOR select sapid,invoice_timestamp,amount,tax,cost 
	,premium_discount,premium_discount_tax,sales_discount,sales_discount_tax,sales_return
	,budget,sales_amount,return_cost,return_amount,contract_amount,contract_cost,gross_profit_rate,month_achievement_rate from
	v_cal_sales_month_achievement where to_char(invoice_timestamp,'YYYYMM')=year_month;
	DELETE FROM SK_SALES_MONTH_ACHIEVEMENT WHERE to_char(baseline_timestamp,'YYYYMM')=year_month;
	-- 提取下一筆指標
	LOOP
		Fetch selectCursor Into  sapid,invoice_timestamp,amount,tax,cost 
	,premium_discount,premium_discount_tax,sales_discount,sales_discount_tax,sales_return
	,budget,sales_amount,return_cost,return_amount,contract_amount,contract_cost,gross_profit_rate,month_achievement_rate;
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
	IF( sales_amount is null ) THEN
		sales_amount := 0;
	END IF;
	IF( return_cost is null ) THEN
		return_cost := 0;
	END IF;
	IF( return_amount is null ) THEN
		return_amount := 0;
	END IF;
	EXIT WHEN selectCursor%NOTFOUND;
	Insert into SK_SALES_MONTH_ACHIEVEMENT(
		ID,BASELINE_TIMESTAMP,SAPID,INVOICE_AMOUNT,PREMIUM_DISCOUNT,
		SALES_DISCOUNT,SALES_RETURN,SALES_AMOUNT,COST,GROSS_PROFIT_RATE,
		BUDGET_MONTH,MONTH_ACHIEVEMENT_RATE,RETURN_COST,RETURN_AMOUNT,CONTRACT_AMOUNT,CONTRACT_COST
		)values(SEQ_SK_ARCH_M.NEXTVAL,invoice_timestamp, sapid, amount, premium_discount,
		sales_discount,sales_return,sales_amount, cost, gross_profit_rate,
		budget,month_achievement_rate,return_cost,return_amount,contract_amount,contract_cost
		);
	END LOOP;
    -- 關閉指標
    Close selectCursor;
    -- 移除釋放指標
    --Deallocate selectCursor;
end p_sales_month_achievement;