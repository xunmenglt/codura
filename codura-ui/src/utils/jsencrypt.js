import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMMJJOG+wzT23ay4pDx7q1Cj6t8O0DDA\n' +
  'sefTTuvSB9Buz4tZGm9p3Dcdo4dvQE2qIX0dsm25M4MdWvXI1cq5fB0CAwEAAQ=='

const privateKey = 'MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAwwkk4b7DNPbdrLik\n' +
  'PHurUKPq3w7QMMCx59NO69IH0G7Pi1kab2ncNx2jh29ATaohfR2ybbkzgx1a9cjV\n' +
  'yrl8HQIDAQABAkEAsSKi7qyVGouDtZVVBOHFLE5TyRbpGv5IFicirKdrFW+V5jrK\n' +
  'yvJMH4yE0++SiawJfr1b6W6PO16EEBjUjBcFOQIhAP0Btoykz3ZdeL4avxBgkjPj\n' +
  'bcoWWeCAwcfYO4h/ZF1LAiEAxVfaYCR4fknpsTfI+coOPMbyS0AuAcYGfMywzI4K\n' +
  'szcCIHWApyx4mpWJYbtApr3Pa/dHR8UUTleS1Oyggz2olI8zAiEAmOX1i1IWjXqE\n' +
  '9srLC0YS++IErmseej9MnXsWbJaNEccCIGuWXeY91/3ltqCUR7fAFutdRP4+Cu4r\n' +
  'K0a5r00v+L4b'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

