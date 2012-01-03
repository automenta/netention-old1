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
 * The area (as defined by Craigslist). This enum contains no "codes", because
 * any code is heavily dependent on the Category.
 *
 * @author Nathan Crause <nathan at crause.name>
 */
public enum Type {

    JOB_OFFERED, JOB_WANTED, HOUSING_OFFERED, HOUSING_WANTED, FOR_SALE,
    ITEM_WANTED, GIG_OFFERED, SERVICE_WANTED, PERSONAL, COMMUNITY, EVENT

}
