/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.content.Intent;
import android.net.Uri;

import com.google.zxing.ZBarcodeFormat;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class DecodeFormatManager {

  private static final Pattern COMMA_PATTERN = Pattern.compile(",");

  public static final Set<ZBarcodeFormat> PRODUCT_FORMATS;
  static final Set<ZBarcodeFormat> INDUSTRIAL_FORMATS;
  private static final Set<ZBarcodeFormat> ONE_D_FORMATS;
  static final Set<ZBarcodeFormat> QR_CODE_FORMATS = EnumSet.of(ZBarcodeFormat.QR_CODE);
  static final Set<ZBarcodeFormat> DATA_MATRIX_FORMATS = EnumSet.of(ZBarcodeFormat.DATA_MATRIX);
  static final Set<ZBarcodeFormat> AZTEC_FORMATS = EnumSet.of(ZBarcodeFormat.AZTEC);
  static final Set<ZBarcodeFormat> PDF417_FORMATS = EnumSet.of(ZBarcodeFormat.PDF_417);
  static {
    PRODUCT_FORMATS = EnumSet.of(ZBarcodeFormat.UPC_A,
                                 ZBarcodeFormat.UPC_E,
                                 ZBarcodeFormat.EAN_13,
                                 ZBarcodeFormat.EAN_8,
                                 ZBarcodeFormat.RSS_14,
                                 ZBarcodeFormat.RSS_EXPANDED);
    INDUSTRIAL_FORMATS = EnumSet.of(ZBarcodeFormat.CODE_39,
                                    ZBarcodeFormat.CODE_93,
                                    ZBarcodeFormat.CODE_128,
                                    ZBarcodeFormat.ITF,
                                    ZBarcodeFormat.CODABAR);
    ONE_D_FORMATS = EnumSet.copyOf(PRODUCT_FORMATS);
    ONE_D_FORMATS.addAll(INDUSTRIAL_FORMATS);
  }
  private static final Map<String,Set<ZBarcodeFormat>> FORMATS_FOR_MODE;
  static {
    FORMATS_FOR_MODE = new HashMap<>();
    FORMATS_FOR_MODE.put(Intents.Scan.ONE_D_MODE, ONE_D_FORMATS);
    FORMATS_FOR_MODE.put(Intents.Scan.PRODUCT_MODE, PRODUCT_FORMATS);
    FORMATS_FOR_MODE.put(Intents.Scan.QR_CODE_MODE, QR_CODE_FORMATS);
    FORMATS_FOR_MODE.put(Intents.Scan.DATA_MATRIX_MODE, DATA_MATRIX_FORMATS);
    FORMATS_FOR_MODE.put(Intents.Scan.AZTEC_MODE, AZTEC_FORMATS);
    FORMATS_FOR_MODE.put(Intents.Scan.PDF417_MODE, PDF417_FORMATS);
  }

  private DecodeFormatManager() {}

  public static Set<ZBarcodeFormat> parseDecodeFormats(Intent intent) {
    Iterable<String> scanFormats = null;
    CharSequence scanFormatsString = intent.getStringExtra(Intents.Scan.FORMATS);
    if (scanFormatsString != null) {
      scanFormats = Arrays.asList(COMMA_PATTERN.split(scanFormatsString));
    }
    return parseDecodeFormats(scanFormats, intent.getStringExtra(Intents.Scan.MODE));
  }

  static Set<ZBarcodeFormat> parseDecodeFormats(Uri inputUri) {
    List<String> formats = inputUri.getQueryParameters(Intents.Scan.FORMATS);
    if (formats != null && formats.size() == 1 && formats.get(0) != null) {
      formats = Arrays.asList(COMMA_PATTERN.split(formats.get(0)));
    }
    return parseDecodeFormats(formats, inputUri.getQueryParameter(Intents.Scan.MODE));
  }

  private static Set<ZBarcodeFormat> parseDecodeFormats(Iterable<String> scanFormats, String decodeMode) {
    if (scanFormats != null) {
      Set<ZBarcodeFormat> formats = EnumSet.noneOf(ZBarcodeFormat.class);
      try {
        for (String format : scanFormats) {
          formats.add(ZBarcodeFormat.valueOf(format));
        }
        return formats;
      } catch (IllegalArgumentException iae) {
        // ignore it then
      }
    }
    if (decodeMode != null) {
      return FORMATS_FOR_MODE.get(decodeMode);
    }
    return null;
  }

}
