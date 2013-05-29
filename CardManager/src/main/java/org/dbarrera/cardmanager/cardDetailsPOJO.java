package org.dbarrera.cardmanager;

/**
 * Created by Dav3 on 5/24/13.
 * Clase temporal para pasar los datos de un metodo a otro en la clase CardNew y CardEdit
 */
public class cardDetailsPOJO {
    private String card_name, card_number, card_ccv, card_type, card_intl, card_debit, card_bank, card_validthru_month, card_validthru_year;
    private int card_type_pos, card_bank_pos;
    private String card_image;

    public String getName(){
        return card_name;
    }

    public void setName(String card_name){
        this.card_name = card_name;
    }

    public String getCardNumber(){
        return card_number;
    }

    public void setCardNumber(String card_number){
        this.card_number = card_number;
    }
    public String getCardCCV(){
        return card_ccv;
    }

    public void setCardCCV(String card_ccv){
        this.card_ccv = card_ccv;
    }
    public String getCardType(){
        return card_type;
    }

    public void setCardType(String card_type){
        this.card_type = card_type;
    }

    public int getCardTypePos(){
        return card_type_pos;
    }

    public void setCardTypePos(int card_type_pos){
        this.card_type_pos = card_type_pos;
    }

    public String getIntl(){
        return card_intl;
    }

    public void setIntl(String card_intl){
        this.card_intl = card_intl;
    }
    public String getDebit(){
        return card_debit;
    }

    public void setDebit(String card_debit){
        this.card_debit = card_debit;
    }

    public String getBank(){
        return card_bank;
    }
    public void setBank(String card_bank){
        this.card_bank = card_bank;
    }

    public int getBankPos(){
        return card_bank_pos;
    }
    public void setBankPos(int card_bank_pos){
        this.card_bank_pos = card_bank_pos;
    }

    public String getValidThruMonth(){
        return card_validthru_month;
    }

    public void setValidThruMonth(String card_validthru_month){
        this.card_validthru_month = card_validthru_month;
    }
    public String getValidThruYear(){
        return card_validthru_year;
    }

    public void setValidThruYear(String card_validthru_year){
        this.card_validthru_year = card_validthru_year;
    }
    public String getImage(){
        return card_image;
    }

    public void setImage(String card_image){
        this.card_image = card_image;
    }
}
