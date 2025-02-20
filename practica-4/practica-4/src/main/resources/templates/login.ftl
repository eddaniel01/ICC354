<!DOCTYPE html>
<html class="h-full bg-white"
      lang="en">
<head>
  <meta charset="UTF-8" />
  <meta
          name="viewport"
          content="width=device-width, initial-scale=1.0" />
  <script src="https://cdn.tailwindcss.com"></script>

  <link
          rel="preconnect"
          href="https://fonts.googleapis.com" />
  <link
          rel="preconnect"
          href="https://fonts.gstatic.com"
          crossorigin />
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

  <title>Iniciar Sesion</title>
</head>
<body class="h-full bg-slate-100">
<div class="flex flex-col justify-center min-h-full py-12 sm:px-6 lg:px-8">
  <div class="sm:mx-auto sm:w-full sm:max-w-md">
    <h2
            class="mt-6 text-2xl font-bold leading-9 tracking-tight text-center text-gray-900">
      Inicie sesion en su cuenta
    </h2>
  </div>

  <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-[480px]">
    <#if error.isPresent()>
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
        <div class="ml-8 mr-12">Usuario o clave incorrecta...</div>
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
    <div class="px-6 py-12 bg-white shadow sm:rounded-lg sm:px-12">
      <form
              role="form"
              class="space-y-6"
              action="/login"
              method="post">
        <#-- Linea para controlar el ataque csrf-->
        <#if _csrf??> <#--validando que no sea nula, si lo es, está deshabilitado el csrf -->
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </#if>
        <div>
          <label
                  for="username"
                  class="block text-sm font-medium leading-6 text-gray-900"
          >Nombre de Usuario</label>
          <div class="mt-2">
            <input
                    id="username"
                    name="username"
                    type="text"
                    required
                    autofocus
                    class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-emerald-600 p-2 sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <label
                  for="password"
                  class="block text-sm font-medium leading-6 text-gray-900"
          >Clave</label
          >
          <div class="mt-2">
            <input
                    id="password"
                    name="password"
                    type="password"
                    required
                    class="block w-full p-2 text-gray-900 border-0 rounded-md shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-emerald-600 sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <button
                  type="submit"
                  class="flex w-full justify-center rounded-md bg-emerald-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-emerald-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-emerald-600">
            Iniciar Sesion
          </button>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
<script src="https://unpkg.com/@material-tailwind/html@latest/scripts/dismissible.js"></script>
</html>