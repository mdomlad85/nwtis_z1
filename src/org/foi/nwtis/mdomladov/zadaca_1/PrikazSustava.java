/*
 * Copyright (C) 2017 Marko Domladovac
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

package org.foi.nwtis.mdomladov.zadaca_1;

import java.util.regex.Pattern;

/**
 *
 * @author Marko Domladovac
 */
class PrikazSustava extends KorisnikSustava {

    public PrikazSustava(String p) {
        sintaksa = "^-prikaz -s ([^\\s]+\\.bin)$";
        poruka = p;
        Pattern pattern = Pattern.compile(sintaksa);
        m = pattern.matcher(poruka);
    }

    @Override
    public String getZahtjev() {
        return m.group(1);
    }

}
