/**************************************************************************************************
 * Copyright (c) 2013-2019 Ashutosh Kumar Singh <ashutosh@aksingh.net>                            *
 *                                                                                                *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this           *
 * software and associated documentation files (the "Software"), to deal in the Software without  *
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,     *
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the  *
 * Software is furnished to do so, subject to the following conditions:                           *
 *                                                                                                *
 * The above copyright notice and this permission notice shall be included in all copies or       *
 * substantial portions of the Software.                                                          *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING  *
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND     *
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,   *
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.        *
 **************************************************************************************************/

package com.telecomdesign.openaq.util

import java.net.*

/**
 * Provides methods for system handling.
 */
internal class SystemTools {

  companion object Static {

    /**
     * Get the system proxy configured
     *
     * @return Proxy default to the system
     */
    @JvmStatic
    fun getSystemProxy(): Proxy {
      var sysProxy = Proxy.NO_PROXY

      var proxyList: List<*>? = null
      try {
        proxyList = ProxySelector.getDefault().select(URI("http://localhost/"))
      } catch (e: URISyntaxException) {
        e.printStackTrace()
      }

      if (proxyList != null) {
        val iter = proxyList.iterator()
        while (iter.hasNext()) {
          sysProxy = iter.next() as java.net.Proxy
        }
      }

      return sysProxy
    }

    /**
     * Set the proxy's authentication details
     *
     * @param user Username for the proxy
     * @param pass Password for the proxy
     */
    @JvmStatic
    fun setProxyAuthDetails(user: String, pass: String) {
      Authenticator.setDefault(object : Authenticator() {
        public override fun getPasswordAuthentication(): PasswordAuthentication {
          return PasswordAuthentication(user, pass.toCharArray())
        }
      })
    }
  }
}
