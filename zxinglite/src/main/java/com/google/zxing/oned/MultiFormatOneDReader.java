/*
 * Copyright 2008 ZXing authors
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

package com.google.zxing.oned;

import com.google.zxing.ZBarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.expanded.RSSExpandedReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class MultiFormatOneDReader extends OneDReader {

  private final OneDReader[] readers;

  public MultiFormatOneDReader(Map<DecodeHintType,?> hints) {
    @SuppressWarnings("unchecked")    
    Collection<ZBarcodeFormat> possibleFormats = hints == null ? null :
        (Collection<ZBarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
    boolean useCode39CheckDigit = hints != null &&
        hints.get(DecodeHintType.ASSUME_CODE_39_CHECK_DIGIT) != null;
    Collection<OneDReader> readers = new ArrayList<>();
    if (possibleFormats != null) {
      if (possibleFormats.contains(ZBarcodeFormat.EAN_13) ||
          possibleFormats.contains(ZBarcodeFormat.UPC_A) ||
          possibleFormats.contains(ZBarcodeFormat.EAN_8) ||
          possibleFormats.contains(ZBarcodeFormat.UPC_E)) {
        readers.add(new MultiFormatUPCEANReader(hints));
      }
      if (possibleFormats.contains(ZBarcodeFormat.CODE_39)) {
        readers.add(new Code39Reader(useCode39CheckDigit));
      }
      if (possibleFormats.contains(ZBarcodeFormat.CODE_93)) {
        readers.add(new Code93Reader());
      }
      if (possibleFormats.contains(ZBarcodeFormat.CODE_128)) {
        readers.add(new Code128Reader());
      }
      if (possibleFormats.contains(ZBarcodeFormat.ITF)) {
         readers.add(new ITFReader());
      }
      if (possibleFormats.contains(ZBarcodeFormat.CODABAR)) {
         readers.add(new CodaBarReader());
      }
      if (possibleFormats.contains(ZBarcodeFormat.RSS_14)) {
         readers.add(new RSS14Reader());
      }
      if (possibleFormats.contains(ZBarcodeFormat.RSS_EXPANDED)) {
        readers.add(new RSSExpandedReader());
      }
    }
    if (readers.isEmpty()) {
      readers.add(new MultiFormatUPCEANReader(hints));
      readers.add(new Code39Reader());
      readers.add(new CodaBarReader());
      readers.add(new Code93Reader());
      readers.add(new Code128Reader());
      readers.add(new ITFReader());
      readers.add(new RSS14Reader());
      readers.add(new RSSExpandedReader());
    }
    this.readers = readers.toArray(new OneDReader[readers.size()]);
  }

  @Override
  public Result decodeRow(int rowNumber,
                          BitArray row,
                          Map<DecodeHintType,?> hints) throws NotFoundException {
    for (OneDReader reader : readers) {
      try {
        return reader.decodeRow(rowNumber, row, hints);
      } catch (ReaderException re) {
        // continue
      }
    }

    throw NotFoundException.getNotFoundInstance();
  }

  @Override
  public void reset() {
    for (Reader reader : readers) {
      reader.reset();
    }
  }

}
