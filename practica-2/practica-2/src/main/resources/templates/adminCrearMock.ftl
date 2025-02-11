<!DOCTYPE html>
<html class="h-full bg-gray-100" lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- Tailwindcss CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- AlpineJS CDN -->
    <script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js"></script>

    <!-- Inter Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
            href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
            rel="stylesheet" />
    <style>
        * {
            font-family: 'Inter', system-ui, -apple-system, BlinkMacSystemFont,
            'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans',
            'Helvetica Neue', sans-serif;
        }
    </style>

    <title>Usuarios</title>
</head>

<body class="h-full">
<div class="min-h-full">
    <div class="pb-32 bg-sky-600">
        <!-- Navigation -->
        <nav class="border-b border-opacity-25 border-sky-300 bg-sky-600"
             x-data="{ mobileMenuOpen: false, userMenuOpen: false }">
            <div class="px-4 mx-auto max-w-7xl sm:px-6 lg:px-8">
                <div class="flex justify-between h-16">
                    <div class="flex items-center px-2 lg:px-0">
                        <div class="hidden sm:block">
                            <div class="flex space-x-4">
                                <!-- Current: "bg-sky-700 text-white", Default: "text-white hover:bg-sky-500 hover:bg-opacity-75" -->
                                <a href="/admin/"
                                   class="px-3 py-2 text-sm font-medium text-white rounded-md hover:bg-sky-500 hover:bg-opacity-75"><@spring.message "users"/></a>
                                <a href="/admin/mocks"
                                   class="px-3 py-2 text-sm font-medium text-white rounded-md hover:bg-sky-500 hover:bg-opacity-75">Mocks</a>
                            </div>
                        </div>
                    </div>
                    <div class="hidden gap-2 sm:ml-6 sm:flex sm:items-center">
                        <!-- Profile dropdown -->
                        <div class="relative ml-3" x-data="{ open: false }">
                            <div>
                                <button @click="open = !open" type="button"
                                        class="flex text-sm bg-white rounded-full focus:outline-none" id="user-menu-button"
                                        aria-expanded="false" aria-haspopup="true">
                                    <span class="sr-only">Open user menu</span>
                                    <span class="inline-flex items-center justify-center w-10 h-10 rounded-full bg-sky-100">
                      <span class="font-medium leading-none text-sky-700">${logged.getNombre()?substring(0, 1)}${logged.getApellido()?substring(0, 1)}</span>
                    </span>
                                    <!-- <img
                                        class="w-10 h-10 rounded-full"
                                        src="https://avatars.githubusercontent.com/u/831997"
                                        alt="Ahmed Shamim Hasan Shaon" /> -->
                                </button>
                            </div>

                            <!-- Dropdown menu -->
                            <div x-show="open" @click.away="open = false"
                                 class="absolute right-0 z-10 w-48 py-1 mt-2 origin-top-right bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
                                 role="menu" aria-orientation="vertical" aria-labelledby="user-menu-button" tabindex="-1">
                                <a href="/logout" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" role="menuitem"
                                   tabindex="-1" id="user-menu-item-2"><@spring.message "sesion"/></a>
                                <a href="/admin/mock?localeData=en" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" role="menuitem"
                                   tabindex="-1" id="user-menu-item-2"><@spring.message "ingles"/></a>
                                <a href="/admin/mock?localeData=es" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" role="menuitem"
                                   tabindex="-1" id="user-menu-item-2"><@spring.message "espanol"/></a>
                            </div>
                        </div>
                    </div>
                    <div class="flex items-center -mr-2 sm:hidden">
                        <!-- Mobile menu button -->
                        <button @click="mobileMenuOpen = !mobileMenuOpen" type="button"
                                class="inline-flex items-center justify-center p-2 rounded-md text-sky-100 hover:bg-sky-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-sky-500"
                                aria-controls="mobile-menu" aria-expanded="false">
                            <span class="sr-only">Open main menu</span>
                            <!-- Icon when menu is closed -->
                            <svg x-show="!mobileMenuOpen" class="block w-6 h-6" xmlns="http://www.w3.org/2000/svg" fill="none"
                                 viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
                            </svg>

                            <!-- Icon when menu is open -->
                            <svg x-show="mobileMenuOpen" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                 stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                </div>
            </div>

            <!-- Mobile menu, show/hide based on menu state. -->
            <div x-show="mobileMenuOpen" class="sm:hidden" id="mobile-menu">
                <div class="pt-2 pb-3 space-y-1">
                    <a href="/admin/"
                       class="block px-3 py-2 text-base font-medium text-white rounded-md hover:bg-sky-500 hover:bg-opacity-75"><@spring.message "users"/></a>
                    <a href="/admin/mocks"
                       class="block px-3 py-2 text-base font-medium text-white rounded-md hover:bg-sky-500 hover:bg-opacity-75">Mocks</a>
                </div>
                <div class="pt-4 pb-3 border-t border-sky-700">
                    <div class="flex items-center px-5">
                        <div class="flex-shrink-0">
                            <!-- <img
                                class="w-10 h-10 rounded-full"
                                src="https://avatars.githubusercontent.com/u/831997"
                                alt="" /> -->
                            <span class="inline-flex items-center justify-center w-10 h-10 rounded-full bg-sky-100">
                  <span class="font-medium leading-none text-sky-700">${logged.getNombre()?substring(0, 1)}${logged.getApellido()?substring(0, 1)}</span>
                </span>
                        </div>
                        <div class="ml-3">
                            <div class="text-base font-medium text-white">
                                ${logged.getNombre()} ${logged.getApellido()}
                            </div>
                            <div class="text-sm font-medium text-sky-300">
                                ${logged.getUsername()}
                            </div>
                        </div>
                    </div>
                    <div class="px-2 mt-3 space-y-1">
                        <a href="/logout"
                           class="block px-3 py-2 text-base font-medium text-white rounded-md hover:bg-sky-500 hover:bg-opacity-75"><@spring.message "sesion"/></a>
                        <a href="/admin/mock?localeData=en"
                           class="block px-3 py-2 text-base font-medium text-white rounded-md hover:bg-sky-500 hover:bg-opacity-75"><@spring.message "ingles"/></a>
                        <a href="/admin/mock?localeData=es"
                           class="block px-3 py-2 text-base font-medium text-white rounded-md hover:bg-sky-500 hover:bg-opacity-75"><@spring.message "espanol"/></a>
                    </div>
                </div>
            </div>
        </nav>

        <header class="py-10">
            <div class="px-4 mx-auto max-w-7xl sm:px-6 lg:px-8">
                <h1 class="text-3xl font-bold tracking-tight text-white">
                    <@spring.message "crear_mock"/>
                </h1>
            </div>
        </header>
    </div>

    <main class="-mt-32">
        <div class="mx-auto max-w-7xl px-4 pb-12 sm:px-6 lg:px-8">
            <div class="bg-white rounded-lg p-2">
                <dl class="mx-auto grid grid-cols-1 gap-px sm:grid-cols-2 lg:grid-cols-4">
                    <div
                            class="flex flex-wrap items-baseline justify-between gap-x-4 gap-y-2 bg-white px-4 py-10 sm:px-6 xl:px-8">
                        <dt class="text-sm font-medium leading-6 text-gray-500">
                            <@spring.message "aceso"/>
                        </dt>
                        <dd class="w-full flex-none text-3xl font-medium leading-10 tracking-tight text-gray-900">
                            <@spring.message "metodos"/>
                        </dd>
                    </div>
                </dl>

                <hr />
                <!-- Deposit Form -->
                <div class="sm:rounded-lg">
                    <div class="px-4 py-5 sm:p-6">
                        <div class="mt-4 text-sm text-gray-500">
                            <#if msg??>
                                <div
                                        class="font-regular relative block w-full rounded-lg bg-gradient-to-tr from-red-600 to-red-400 px-4 py-4 text-base text-white"
                                        data-dismissible="alert"
                                >
                                    <div class="absolute top-4 left-4">
                                        <svg
                                                xmlns="http://www.w3.org/2000/svg"
                                                viewBox="0 0 24 24"
                                                fill="currentColor"
                                                aria-hidden="true"
                                                class="h-6 w-6"
                                        >
                                            <path
                                                    fill-rule="evenodd"
                                                    d="M9.401 3.003c1.155-2 4.043-2 5.197 0l7.355 12.748c1.154 2-.29 4.5-2.599 4.5H4.645c-2.309 0-3.752-2.5-2.598-4.5L9.4 3.003zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z"
                                                    clip-rule="evenodd"
                                            ></path>
                                        </svg>
                                    </div>
                                    <div class="ml-8 mr-12">${msg}</div>
                                    <div
                                            class="absolute top-3 right-3 w-max rounded-lg transition-all hover:bg-white hover:bg-opacity-20"
                                            data-dismissible-target="alert"
                                    >
                                        <div role="button" class="w-max rounded-lg p-1">
                                            <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    class="h-6 w-6"
                                                    fill="none"
                                                    viewBox="0 0 24 24"
                                                    stroke="currentColor"
                                                    stroke-width="2"
                                            >
                                                <path
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                        d="M6 18L18 6M6 6l12 12"
                                                ></path>
                                            </svg>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                            <#if msgSuccess??>
                                <div
                                        class="font-regular relative block w-full rounded-lg bg-gradient-to-tr from-green-600 to-green-400 px-4 py-4 text-base text-white"
                                        data-dismissible="alert"
                                >
                                    <div class="absolute top-4 left-4">
                                        <svg
                                                xmlns="http://www.w3.org/2000/svg"
                                                viewBox="0 0 24 24"
                                                fill="currentColor"
                                                aria-hidden="true"
                                                class="mt-px h-6 w-6"
                                        >
                                            <path
                                                    fill-rule="evenodd"
                                                    d="M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zm13.36-1.814a.75.75 0 10-1.22-.872l-3.236 4.53L9.53 12.22a.75.75 0 00-1.06 1.06l2.25 2.25a.75.75 0 001.14-.094l3.75-5.25z"
                                                    clip-rule="evenodd"
                                            ></path>
                                        </svg>
                                    </div>
                                    <div class="ml-8 mr-12">${msgSuccess}</div>
                                    <div
                                            class="absolute top-3 right-3 w-max rounded-lg transition-all hover:bg-white hover:bg-opacity-20"
                                            data-dismissible-target="alert"
                                    >
                                        <div role="button" class="w-max rounded-lg p-1">
                                            <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    class="h-6 w-6"
                                                    fill="none"
                                                    viewBox="0 0 24 24"
                                                    stroke="currentColor"
                                                    stroke-width="2"
                                            >
                                                <path
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                        d="M6 18L18 6M6 6l12 12"
                                                ></path>
                                            </svg>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                            <#if datos??>
                                <form name="myForm" role="form" action="/admin/mock?id=${datos.getId()}" method="POST" onsubmit="return validateForm()">
                                    <#if _csrf??> <#--validando que no sea nula, si lo es, está deshabilitado el csrf -->
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </#if>
                                    <!-- Input Field -->
                                    <div class="relative mt-2 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "nombre"/>
                                        </h3>
                                        <input type="text" name="nombre" id="nombre" value="${datos.getNombre()}"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                               placeholder=<@spring.message "nombre"/> required />
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "descripcion"/>
                                        </h3>
                                        <input type="text" name="descripcion" id="descripcion" value="${datos.getDescripcion()}"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                               placeholder=<@spring.message "descripcion"/> required />
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "ruta"/>
                                        </h3>
                                        <div class="pointer-events-none absolute left-0 flex items-center pl-0 py-2">
                                            <span class="text-gray-400">http://${hostname}:${port}/mock/</span>
                                        </div>
                                        <input type="text" name="ruta" id="ruta" value="${datos.getRuta()}"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400 pl-48"
                                               placeholder=<@spring.message "ruta"/> required />
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "codes"/>
                                        </h3>
                                        <select
                                                class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                name="codigo" id="codigo" required>
                                            <#list ["100 - Continue", "101 - Switching Protocols", "102 - Processing", "200 - OK", "201 - Created", "202- Accepted", "203 - Non-Authoritative Information", "204 - No Content", "205 - Reset Content", "206 - Partial Content", "207 - Multi-Status", "208 - Already Reported", "226 - IM Used", "300 - Multiple Choices", "301 - Moved Permanently", "302 - Found", "303 - See Other", "304 - Not Modified", "305 - Use Proxy", "306 - Switch Proxy", "307 - Temporary Redirect", "308 - Permanent Redirect", "400 - Bad Request", "401 - Unauthorized", "402 - Payment Required", "403 - Forbidden", "404 - Not Found", "405 - Method Not Allowed", "406 - Not Acceptable", "407 - Proxy Authentication Required", "408 - Request Timeout", "409 - Conflict", "410 - Gone", "411 - Length Required", "412 - Precondition Failed", "413 - Content Too Large", "414 - URI Too Long", "415 - Unsupported Media Type", "416 - Range Not Satisfiable", "417 - Expectation Failed", "418 - I'm a teapot", "421 - Misdirected Request", "422 - Unprocessable Content", "423 - Locked", "424 - Failed Dependency", "425 - Too Early", "426 - Upgrade Required", "428 - Precondition Required", "429 - Too Many Requests", "431 - Request Header Fields Too Large", "451 - Unavailable For Legal Reasons", "500 - Internal Server Error", "501 - Not Implemented", "502 - Bad Gateway", "503 - Service Unavailable", "504 - Gateway Timeout", "505 - HTTP Version Not Supported", "506 - Variant Also Negotiates", "507 - Insufficient Storage", "508 - Loop Detected", "510 - Not Extended", "511 - Network Authentication Required"] as codigo>
                                                <#if datos.getCodigo() == codigo>
                                                    <option value="${codigo}" selected>${codigo}</option>
                                                <#else>
                                                    <option value="${codigo}">${codigo}</option>
                                                </#if>
                                            </#list>
                                        </select>
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "content"/>
                                        </h3>
                                        <select
                                                class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                name="contentType" id="contentType" required>
                                            <#list ["application/EDI-X12", "application/EDIFACT", "application/javascript", "application/octet-stream", "application/ogg", "application/pdf", "application/xhtml+xml", "application/x-shockwave-flash", "application/json", "application/ld+json", "application/xml", "application/zip", "application/x-www-form-urlencoded", "audio/mpeg", "audio/x-ms-wma", "audio/vnd.rn-realaudio", "audio/x-wav", "image/gif", "image/jpeg", "image/png", "image/tiff", "image/vnd.microsoft.icon", "image/x-icon", "image/vnd.djvu", "image/svg+xml", "multipart/mixed", "multipart/alternative", "multipart/related", "multipart/form-data", "text/css", "text/csv", "text/html", "text/javascript", "text/plain", "text/xml", "video/mpeg", "video/mp4", "video/quicktime", "video/x-ms-wmv", "video/x-msvideo", "video/x-flv", "video/webm", "application/vnd.oasis.opendocument.text", "application/vnd.oasis.opendocument.spreadsheet", "application/vnd.oasis.opendocument.presentation", "application/vnd.oasis.opendocument.graphics", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.mozilla.xul+xml"] as contentType>
                                                <#if datos.getContentType() == contentType>
                                                    <option value="${contentType}" selected>${contentType}</option>
                                                <#else>
                                                    <option value="${contentType}">${contentType}</option>
                                                </#if>
                                            </#list>
                                        </select>
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "headers"/>
                                        </h3>
                                        <textarea id="headers" name="headers" rows="3"
                                                  class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                  placeholder='{
	"X-Foo-Bar": "Hello World"
}'>${datos.getHeaders()}</textarea>
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "cuerpo"/>
                                        </h3>
                                        <textarea id="cuerpo" name="cuerpo" rows="11"
                                                  class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                  placeholder='{
      "identity":{
        "id": "10141505",
        "login": "John Doe"
      },
      "permissions":{
        "roles":[
          "moderator"
        ]
      }
}'>${datos.getCuerpo()}</textarea>
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "demora"/>
                                        </h3>
                                        <input type="number" name="demora" id="demora" value="${datos.getDemora()}"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                               placeholder=<@spring.message "demora"/> required />
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "tiempo"/>
                                        </h3>
                                        <select
                                                class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                name="expira" id="expira" required>
                                            <#if datos.getExpira() == "8760">
                                                <option value="8760" selected><@spring.message "year"/></option>
                                            <#else>
                                                <option value="8760"><@spring.message "year"/></option>
                                            </#if>
                                            <#if datos.getExpira() == "1">
                                                <option value="1" selected><@spring.message "hour"/></option>
                                            <#else>
                                                <option value="1"><@spring.message "hour"/></option>
                                            </#if>
                                            <#if datos.getExpira() == "24">
                                                <option value="24" selected><@spring.message "day"/></option>
                                            <#else>
                                                <option value="24"><@spring.message "day"/></option>
                                            </#if>
                                            <#if datos.getExpira() == "168">
                                                <option value="168" selected><@spring.message "week"/></option>
                                            <#else>
                                                <option value="168"><@spring.message "week"/></option>
                                            </#if>
                                            <#if datos.getExpira() == "730">
                                                <option value="730" selected><@spring.message "month"/></option>
                                            <#else>
                                                <option value="730"><@spring.message "month"/></option>
                                            </#if>
                                        </select>
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "jwt"/> http://${hostname}:${port}/mock/jwt/<@spring.message "route_of_your_mock"/>}
                                        </h3>
                                    </div>

                                    <div class="mt-5">
                                        <#if userMock??>
                                        <a href="/admin/mocks?id=${userMock.getId()}" type="button"
                                           class="w-full px-6 py-3.5 text-base font-medium text-white bg-sky-600 hover:bg-sky-800 focus:ring-4 focus:outline-none focus:ring-sky-300 rounded-lg sm:text-xl text-center">
                                            <@spring.message "button_cancel"/>
                                        </a>
                                        <#else>
                                        <button type="submit"
                                                class="w-full px-6 py-3.5 text-base font-medium text-white bg-sky-600 hover:bg-sky-800 focus:ring-4 focus:outline-none focus:ring-sky-300 rounded-lg sm:text-xl text-center">
                                            <@spring.message "button"/>
                                        </button>
                                    </div>
                                    </#if>
                                </form>
                            <#else>
                                <form name="myForm" role="form" action="/admin/mock" method="POST" onsubmit="return validateForm()">
                                    <#if _csrf??> <#--validando que no sea nula, si lo es, está deshabilitado el csrf -->
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </#if>
                                    <!-- Input Field -->
                                    <div class="relative mt-2 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "nombre"/>
                                        </h3>
                                        <input type="text" name="nombre" id="nombre"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                               placeholder="<@spring.message "nombre"/>" required />
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "descripcion"/>
                                        </h3>
                                        <input type="text" name="descripcion" id="descripcion"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                               placeholder=<@spring.message "descripcion"/> required />
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "ruta"/>
                                        </h3>
                                        <div class="pointer-events-none absolute left-0 flex items-center pl-0 py-2">
                                            <span class="text-gray-400">http://${hostname}:${port}/mock/</span>
                                        </div>
                                        <input type="text" name="ruta" id="ruta"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400 pl-48"
                                               placeholder=<@spring.message "ruta"/> required />
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "codes"/>
                                        </h3>
                                        <select
                                                class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                name="codigo" id="codigo" required>
                                            <#list ["100 - Continue", "101 - Switching Protocols", "102 - Processing", "200 - OK", "201 - Created", "202- Accepted", "203 - Non-Authoritative Information", "204 - No Content", "205 - Reset Content", "206 - Partial Content", "207 - Multi-Status", "208 - Already Reported", "226 - IM Used", "300 - Multiple Choices", "301 - Moved Permanently", "302 - Found", "303 - See Other", "304 - Not Modified", "305 - Use Proxy", "306 - Switch Proxy", "307 - Temporary Redirect", "308 - Permanent Redirect", "400 - Bad Request", "401 - Unauthorized", "402 - Payment Required", "403 - Forbidden", "404 - Not Found", "405 - Method Not Allowed", "406 - Not Acceptable", "407 - Proxy Authentication Required", "408 - Request Timeout", "409 - Conflict", "410 - Gone", "411 - Length Required", "412 - Precondition Failed", "413 - Content Too Large", "414 - URI Too Long", "415 - Unsupported Media Type", "416 - Range Not Satisfiable", "417 - Expectation Failed", "418 - I'm a teapot", "421 - Misdirected Request", "422 - Unprocessable Content", "423 - Locked", "424 - Failed Dependency", "425 - Too Early", "426 - Upgrade Required", "428 - Precondition Required", "429 - Too Many Requests", "431 - Request Header Fields Too Large", "451 - Unavailable For Legal Reasons", "500 - Internal Server Error", "501 - Not Implemented", "502 - Bad Gateway", "503 - Service Unavailable", "504 - Gateway Timeout", "505 - HTTP Version Not Supported", "506 - Variant Also Negotiates", "507 - Insufficient Storage", "508 - Loop Detected", "510 - Not Extended", "511 - Network Authentication Required"] as codigo>
                                                <option value="${codigo}">${codigo}</option>
                                            </#list>
                                        </select>
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "content"/>
                                        </h3>
                                        <select
                                                class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                name="contentType" id="contentType" required>
                                            <#list ["application/EDI-X12", "application/EDIFACT", "application/javascript", "application/octet-stream", "application/ogg", "application/pdf", "application/xhtml+xml", "application/x-shockwave-flash", "application/json", "application/ld+json", "application/xml", "application/zip", "application/x-www-form-urlencoded", "audio/mpeg", "audio/x-ms-wma", "audio/vnd.rn-realaudio", "audio/x-wav", "image/gif", "image/jpeg", "image/png", "image/tiff", "image/vnd.microsoft.icon", "image/x-icon", "image/vnd.djvu", "image/svg+xml", "multipart/mixed", "multipart/alternative", "multipart/related", "multipart/form-data", "text/css", "text/csv", "text/html", "text/javascript", "text/plain", "text/xml", "video/mpeg", "video/mp4", "video/quicktime", "video/x-ms-wmv", "video/x-msvideo", "video/x-flv", "video/webm", "application/vnd.oasis.opendocument.text", "application/vnd.oasis.opendocument.spreadsheet", "application/vnd.oasis.opendocument.presentation", "application/vnd.oasis.opendocument.graphics", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.mozilla.xul+xml"] as contentType>
                                                <option value="${contentType}">${contentType}</option>
                                            </#list>
                                        </select>
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "headers"/>
                                        </h3>
                                        <textarea id="headers" name="headers" rows="3"
                                                  class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                  placeholder='{
      "X-Foo-Bar": "Hello World"
}'></textarea>
                                    </div>

                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "cuerpo"/>
                                        </h3>
                                        <textarea id="cuerpo" name="cuerpo" rows="11"
                                                  class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                  placeholder='{
      "identity":{
        "id": "10141505",
        "login": "John Doe"
      },
      "permissions":{
        "roles":[
          "moderator"
        ]
      }
}'></textarea>
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "demora"/>
                                        </h3>
                                        <input type="number" name="demora" id="demora" value="0"
                                               class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                               placeholder=<@spring.message "demora"/> required />
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "tiempo"/>
                                        </h3>
                                        <select
                                                class="block w-full ring-0 outline-none py-2 text-gray-800 border-b border-b-sky-500 placeholder:text-gray-400"
                                                name="expira" id="expira" required>
                                            <option value="8760"><@spring.message "year"/></option>
                                            <option value="1"><@spring.message "hour"/></option>
                                            <option value="24"><@spring.message "day"/></option>
                                            <option value="168"><@spring.message "week"/></option>
                                            <option value="730"><@spring.message "month"/></option>
                                        </select>
                                    </div>
                                    <div class="relative mt-4 md:mt-8 rounded-md">
                                        <h3 class="text-lg font-semibold leading-6 text-gray-800">
                                            <@spring.message "jwt"/> http://${hostname}:${port}/mock/jwt/<@spring.message "route_of_your_mock"/>
                                        </h3>
                                    </div>

                                    <!-- Submit Button -->
                                    <div class="mt-5">
                                        <button type="submit"
                                                class="w-full px-6 py-3.5 text-base font-medium text-white bg-sky-600 hover:bg-sky-800 focus:ring-4 focus:outline-none focus:ring-sky-300 rounded-lg sm:text-xl text-center">
                                            <@spring.message "button"/>
                                        </button>
                                    </div>
                                </form>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
<script src="https://unpkg.com/@material-tailwind/html@latest/scripts/dismissible.js"></script>
<script>
    function validateForm() {
        let item = document.forms["myForm"]["headers"].value;
        if (item === "") {
            return true;
        }else {
            let value = typeof item !== "string" ? JSON.stringify(item) : item;
            try {
                value = JSON.parse(value);
            } catch (e) {
                alert("Los headers deben estar en formato JSON");
                return false;
            }
            let y = typeof value === "object" && value !== null;
            if (!y){
                alert("Los headers deben estar en formato JSON");
                return y;
            }
            return y;
        }
    }
</script>
</html>