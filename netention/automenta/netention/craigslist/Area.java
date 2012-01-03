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

/**
 * The area (as defined by Craigslist). Use "getCode()" to retrieve the
 * underlying 3-character Craigslist code.
 *
 * @author Nathan Crause <nathan at clarkesolomou.com>
 */
public enum Area {

    NANAIMO("nmo"),
    VICTORIA("vic");

    private String code;

    Area(String code) {
        this.code = code;
    }

    /**
     * Gets the 3-character Craigslist code for this area
     *
     * @return 3-character code
     */
    public String getCode() {
        return code;
    }

    /**
     * Finds the Area enum which has the specified 3-character code.
     *
     * @param code the code to look for
     * @return the matching enum
     * @throws IllegalAccessException if the code could not be found
     */
    public static Area codeOf(String code) throws IllegalAccessException {
        for (Area area : values())
            if (area.getCode().equals(code))
                return area;

        throw new IllegalAccessException("No area with code '" + code + "' found");
    }

}
