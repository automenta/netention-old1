/*
 * Copyright (C) 2011 Clarke, Solomou & Associates Microsystems Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package automenta.netention.craigslist;

import java.util.Arrays;
import java.util.List;

/**
 * The category within a Type (as defined by Craigslist). Usage typically
 * requires combining it with the Type in order to get meaningful results. To
 * this end, it's "getCode()" is a great deal more complex than Area and Type.
 * <p>
 * For example, "Housing Offered" and "Housing Wanted" have the same "code" (H),
 * but with an additional parameter (wanted=y/n) specified in the URL. Once
 * we select either, the category is a 3-character code directly off of "H".
 * Thus, we cannot unique distinguish between the 2 types separated from the
 * categories within each.
 *
 * @author Nathan Crause <nathan at clarkesolomou.com>
 */
public enum Category {

    NULL,   // use this category when the posting type doesn't have any (such as resumes)

    ACCOUNTING_OR_FINANCE, 
    ADMIN_OR_OFFICE,
    ARCHITECT_OR_ENGINEER_OR_CAD,
    ART_OR_MEDIA_OR_DESIGN,
    BUSINESS_OR_MANAGEMENT,
    CUSTOMER_SERVICE,
    EDUCATION_OR_TEACHING,
    ETC,
    FOOD_OR_BEVERAGE_OR_HOSPITALITY,
    GENERAL_LABOUR,
    GOVERNMENT,
    HEALTHCARE,
    HUMAN_RESOURCES,
    INTERNET_ENGINEERING,
    LEGAL_OR_PARALEGAL,
    MANUFACTURING,
    MARKETING_OR_ADVERTISING_OR_PR,
    NONPROFIT,
    REAL_ESTATE,
    RETAIL_OR_WHOLESALE,
    SALES,
    SALON_OR_SPA_OR_FITNESS,
    SCIENCE_OR_BIOTECH,
    SECURITY,
    SKILLED_TRADES_OR_ARTISAN,
    SOFTWARE_OR_QA_OR_DBA_ETC,
    SYSTEMS_OR_NETWORKING,
    TECHNICAL_SUPPORT,
    TRANSPORTATION,
    TV_OR_FILM_OR_RADIO,
    WEB_OR_HTML_OR_INFO_DESIGN,
    WRITING_OR_EDITING,

    ROOMS_AND_SHARES,
    FOR_RENT,
    SWAP,
    OFFICE_AND_COMMERCIAL,
    PARKING_AND_STORAGE,
    REAL_ESTATE_BY_BROKEN,
    REAL_ESTATE_BY_OWNER,
    SUBLETS_AND_TEMPORARY,
    VACATION_RENTALS;

    private static List<Category> VALID_WITH_JOB_OFFERED = Arrays.asList(
                        ACCOUNTING_OR_FINANCE,
                        ADMIN_OR_OFFICE,
                        ARCHITECT_OR_ENGINEER_OR_CAD,
                        ART_OR_MEDIA_OR_DESIGN,
                        BUSINESS_OR_MANAGEMENT,
                        CUSTOMER_SERVICE,
                        EDUCATION_OR_TEACHING,
                        ETC,
                        FOOD_OR_BEVERAGE_OR_HOSPITALITY,
                        GENERAL_LABOUR,
                        GOVERNMENT,
                        HEALTHCARE,
                        HUMAN_RESOURCES,
                        INTERNET_ENGINEERING,
                        LEGAL_OR_PARALEGAL,
                        MANUFACTURING,
                        MARKETING_OR_ADVERTISING_OR_PR,
                        NONPROFIT,
                        REAL_ESTATE,
                        RETAIL_OR_WHOLESALE,
                        SALES,
                        SALON_OR_SPA_OR_FITNESS,
                        SCIENCE_OR_BIOTECH,
                        SECURITY,
                        SKILLED_TRADES_OR_ARTISAN,
                        SOFTWARE_OR_QA_OR_DBA_ETC,
                        SYSTEMS_OR_NETWORKING,
                        TECHNICAL_SUPPORT,
                        TRANSPORTATION,
                        TV_OR_FILM_OR_RADIO,
                        WEB_OR_HTML_OR_INFO_DESIGN,
                        WRITING_OR_EDITING
                        );

    private static List<Category> VALID_WITH_JOB_WANTED = Arrays.asList(NULL);

    /**
     * Returns a URL path fragment based on the this category in combination
     * with the type
     *
     * @param type the type to combine with this category (must be a valid
     * combination)
     * @return the URL path fragment
     * @throws IllegalArgumentException if the supplied Type does not form a
     * valid combination with this category
     * @see Category#isValidWith(com.csam.craigslist.Type)
     */
    public String getPathFragment(Type type) {
        if (!isValidWith(type))
            throw new IllegalArgumentException("Category " + this
                    + " is invalid with posting type " + type);
        
        return null;
    }

    /**
     * Tests if the supplied type provides a valid combination with this
     * category.
     *
     * @param type the Type to test with
     * @return true if the combination is valid, false otherwise
     */
    public boolean isValidWith(Type type) {
        switch (type) {
            case JOB_OFFERED:
                return VALID_WITH_JOB_OFFERED.contains(this);
            case JOB_WANTED:
                return VALID_WITH_JOB_WANTED.contains(this);
        }

        return false;
    }

}
