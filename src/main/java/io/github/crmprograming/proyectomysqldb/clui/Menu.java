package io.github.crmprograming.proyectomysqldb.clui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import io.github.crmprograming.proyectomysqldb.nui.Conexion;
import io.github.crmprograming.proyectomysqldb.nui.Equipo;
import io.github.crmprograming.proyectomysqldb.nui.Registro;

/**
 * Clase gestora de la interfaz de la aplicación.
 */
public abstract class Menu {

	/**
	 * Método encargado de mostrar el menú inicial de la aplicación.
	 */
	private static void mostrarMenuInicial() {
		System.out.println();
		System.out.println("########################");
		System.out.printf("#%22s#%n", "");
		System.out.printf("%-8s%-8s%8s%n", "#", Conexion.descripcionConexion[Conexion.conexionDefinida.getId()], "#");
		System.out.printf("#%22s#%n", "");
		System.out.println("########################");
		System.out.println();
		System.out.println("- Autor: César Ravelo Martínez\n");

		System.out.println("1) Visualizar todos los datos de los equipos");
		System.out.println("2) Insertar equipo");
		System.out.println("3) Eliminar equipo");
		System.out.println("4) Modificar los datos de un equipo");
		if (Conexion.conexionDefinida != Conexion.TIPO_CONEXION.ACCESS)
			System.out.println("5) Funciones adicionales");
		System.out.println("0) Salir");
		System.out.print("\n> Introduzca la opción a ejecutar: ");
	}

	/**
	 * Método encargado de gestionar el menú principal de la aplicación.
	 * @param in 
	 */
	private static void gestionarMenuInicial(Scanner in) {
		ArrayList<Registro> listado;
		int opc = -1;
		String[] _error;
		String nomEquipo, codLiga, localidad, dni;
		boolean internacional;
		int idEquipo;
		Equipo actual;
		int activosPrecioAnual, activosPrecioRecision;
		int[] _result;
		int totalMeses;

		do {
			_error = new String[] { "" };
			mostrarMenuInicial();
			opc = in.nextInt();

			switch (opc) {
			case 0:
				break;
			case 1: // Mostrar listado
				listado = Conexion.obtenerListadoEquipos(_error);

				if (_error[0].equals(""))
					mostrarTablaEquipos(listado);
				break;

			case 2: // Insertar equipo
				opc = -1;
				do {
					mostrarMenuInsertarEquipo();
					opc = in.nextInt();

					switch (opc) {
					case 0:
						break;
					case 1: // Pedir datos del equipo
						in.nextLine();

						System.out.print("> Nombre del equipo (LIMIT 40): ");
						nomEquipo = in.nextLine();
						System.out.print("> Código de la liga (LIMIT 5): ");
						codLiga = in.nextLine();
						System.out.print("> Localidad del equipo (LIMIT 60): ");
						localidad = in.nextLine();
						System.out.print("> ¿El equipo es internacional? (s|n): ");
						internacional = in.nextLine().toUpperCase().equals("S");
						System.out.println();

						if (Conexion.insertarEquipo(new Equipo(-1, nomEquipo, codLiga, null, localidad, internacional), _error))
							System.out.println("Se ha dado de alta el equipo " + nomEquipo + " sin problemas.");
						break;
					case 2: // Mostrar ligas y sus códigos
						listado = Conexion.obtenerListadoLigas(_error);

						if (_error[0].equals(""))
							mostrarTablaLigas(listado);
						break;

					default:
						System.out.println("- Opción introducida no válida");
					}

				} while (opc != 0 && _error[0].equals(""));
				opc = 2;
				break;

			case 3: // Eliminar Equipo
				do {
					System.out.print("> Introduzca el ID del equipo a eliminar o -1 para cancelar: ");
					idEquipo = in.nextInt();

					if (idEquipo != -1) {
						if (Conexion.isEquipoExiste(idEquipo, _error)) {
							actual = Conexion.obtenerEquipo(idEquipo, _error);
							mostrarTablaEquipos(new ArrayList<Registro>(Arrays.asList(actual)));
							if (Conexion.tieneContratos(idEquipo, _error)) {
								System.out.println("\nEl equipo indicado tiene contratos vinculados a él.");
							}
							if (confirmarBorrado(in) && Conexion.borrarEquipo(idEquipo, _error))
								System.out.printf("Se ha borrado el equipo %d correctamente%n", idEquipo);
						} else if (_error[0].equals(""))
							System.out.println("No existe ningún equipo con el id " + idEquipo);
					}

					if (!_error[0].equals("")) {
						System.err.println(_error[0]);
						_error[0] = "";
					}

				} while (idEquipo != -1);

				break;

			case 4: // Modificar Equipo
				do {
					System.out.print("> Introduzca el ID del equipo a modificar o -1 para cancelar: ");
					idEquipo = in.nextInt();

					if (idEquipo != -1) {
						if (Conexion.isEquipoExiste(idEquipo, _error)) {
							actual = Conexion.obtenerEquipo(idEquipo, _error);

							if (actual != null) {
								in.nextLine();

								System.out.println("\nIndique a continuación los nuevos datos del equipo.");
								System.out.println("Los actuales vendrán indicados entre paréntesis\n");

								System.out.printf("> Nombre del equipo (LIMIT 40) (%s): ", actual.getNomEquipo());
								nomEquipo = in.nextLine();
								System.out.printf("> Código de la liga (LIMIT 5) (%s): ", actual.getCodLiga());
								codLiga = in.nextLine();
								System.out.printf("> Localidad del equipo (LIMIT 60) (%s): ", actual.getLocalidad());
								localidad = in.nextLine();
								System.out.printf("> ¿El equipo es internacional? (s|n) (%s): ", (actual.isInternacional()) ? "S" : "N");
								internacional = in.nextLine().toUpperCase().equals("S");
								System.out.println();

								if (Conexion.modificarEquipo(new Equipo(actual.getCodEquipo(), nomEquipo, codLiga, null, localidad, internacional), _error))
									System.out.println("Se han actualizado los datos correctamente.");
								else
									System.out.println("No se pudieron actualizar los datos.");
							}
						} else if (_error[0].equals(""))
							System.out.println("No existe ningún equipo con el id " + idEquipo);
						
						if (!_error[0].equals("")) {
							System.err.println(_error[0]);
							_error[0] = "";
						}
					}

				} while (idEquipo != -1);
				break;
				
			case 5: // Funcionalidades adicionales
				if (Conexion.conexionDefinida != Conexion.TIPO_CONEXION.ACCESS) {
					opc = -1;
					do {
						mostrarMenuFunciones();
						opc = in.nextInt();
	
						switch (opc) {
						case 0:
							break;
						case 1: // Pedir datos del equipo
							in.nextLine();
	
							System.out.print("> Nombre del equipo (LIMIT 40): ");
							nomEquipo = in.nextLine();
							System.out.print("> Código de la liga (LIMIT 5): ");
							codLiga = in.nextLine();
							System.out.print("> Localidad del equipo (LIMIT 60): ");
							localidad = in.nextLine();
							System.out.print("> ¿El equipo es internacional? (s|n): ");
							internacional = in.nextLine().toUpperCase().equals("S");
							System.out.println();
	
							if (Conexion.insertarEquipoFunciones(new Equipo(-1, nomEquipo, codLiga, null, localidad, internacional), _error))
								System.out.println("Se ha dado de alta el equipo " + nomEquipo + " sin problemas.");
							break;
						case 2: // Visualizar todos los contratos
							in.nextLine();
							
							System.out.print("> Indique el DNI o NIE del futbolista (-1 para cancelar): ");
							dni = in.nextLine();
							if (!dni.equals("-1")) {
								listado = Conexion.obtenerContratosFutbolista(dni, _error);
								
								if (_error[0].equals("")) {
									mostrarTablaContratos(listado);
									dni = "-1";
								}
							}
							
							break;
							
						case 3: // Visualizar futbolistas en activo
							System.out.print("> Introduzca el ID del equipo a mostrar sus jugadores en activo o -1 para cancelar: ");
							idEquipo = in.nextInt();
	
							System.out.print("> Indique el precio anual del jugador: ");
							activosPrecioAnual = in.nextInt();
								
							System.out.print("> Indique el precio de recisión: ");
							activosPrecioRecision = in.nextInt();
								
							_result = Conexion.obtenerContratosEnActivo(idEquipo, activosPrecioAnual, activosPrecioRecision, _error);
	
							if (_error[0].equals(""))
								mostrarTablaContratosActivos(_result);
							
							break;
							
						case 4: // Mostrar total de meses activos de un futbolista
							in.nextLine();
							
							System.out.print("> Indique el DNI o NIE del futbolista (-1 para cancelar): ");
							dni = in.nextLine();
							if (!dni.equals("-1")) {
								totalMeses = Conexion.obtenerMesesActivosFutbolista(dni, _error);
								
								if (_error[0].equals("")) {
									mostrarTotalMesesActivo(totalMeses);
									dni = "-1";
								}
							}
							break;
	
						default:
							System.out.println("- Opción introducida no válida");
						}
	
					} while (opc != 0 && _error[0].equals(""));
					opc = 5;
				}
				break;

			default:
				System.out.println("- Opción introducida no válida");
			}

			if (!_error[0].equals(""))
				System.err.println(_error[0]);

		} while (opc != 0);
		in.close();
	}

	/**
	 * Método encargado de mostrar el menú para insertar un equipo.
	 */
	private static void mostrarMenuInsertarEquipo() {
		System.out.println();
		System.out.println("########################");
		System.out.printf("#%22s#%n", "");
		System.out.println("#    INSERTAR EQUIPO   #");
		System.out.printf("#%22s#%n", "");
		System.out.println("########################");
		System.out.println();
		System.out.println("- Autor: César Ravelo Martínez\n");
		
		System.out.println("1) Dar datos del equipo");
		System.out.println("2) Mostrar ligas con sus identificadores");
		System.out.println("0) Cancelar operación");
		System.out.print("\n> Introduzca la opción a ejecutar: ");
	}

	/**
	 * Método encargado de mostrar el listado de equipos.
	 * Delega la tarea de dar formato a la tabla a la función mostrarTablaDatos.
	 * 
	 * @param listado ArrayList con los datos a mostrar
	 * @see mostrarTablaDatos
	 */
	private static void mostrarTablaEquipos(ArrayList<Registro> listado) {
		mostrarTablaDatos(listado, new String[] {"codEquipo", "nomEquipo", "nomLiga", "localidad", "internacional"}, "%n%11s | %-40s | %-50s | %-60s | %-15s");
	}
	
	/**
	 * Método encargado de mostrar el listado de ligas.
	 * Delega la tarea de dar formato a la tabla a la función mostrarTablaDatos.
	 *
	 * @param listado ArrayList con los datos a mostrar
	 * @see mostrarTablaDatos
	 */
	private static void mostrarTablaLigas(ArrayList<Registro> listado) {
		mostrarTablaDatos(listado, new String[] {"codLiga", "nomLiga"}, "%n%10s | %-50s");
	}
	
	/**
	 * Método encargado de construir una tabla.
	 * Gracias al formato indicado por parámetro, construirá la tabla
	 * independiente de los datos que le vengan.
	 * La cabecera reaparece cada 5 filas mostradas.
	 * 
	 * @param listado ArrayList con los datos a mostrar
	 * @param _cabecera Array con las cabeceras en String de cada columna
	 * @param formato String con el formato preparado asociado a los datos a mostrar
	 */
	private static void mostrarTablaDatos(ArrayList<Registro> listado, Object[] _cabecera, String formato) {
		int i;
		
		if (listado.isEmpty()) {
			System.out.printf("%n" + formato, _cabecera);
			System.out.printf("%nNo hay valores que mostrar\n");
		} else {
			for (i = 0; i < listado.size(); i++) {			
				if (i % 5 == 0)
					System.out.printf("%n" + formato, _cabecera);
				System.out.printf(formato, listado.get(i).obtenerDatos());			
			}
			System.out.println();
		}
	}
	
	
	/**
	 * Método encargado de confirmar si el usuario desea borrar un equipo
	 * o anular la operación.
	 *
	 * @param in Instancia de Scanner que capturará la decisión del usuario
	 * @return true, si el usuario confirma
	 */
	private static boolean confirmarBorrado(Scanner in) {
		in.nextLine();
		System.out.println();
		System.out.print("> ¿Está seguro de que quiere borrar el equipo y todo lo relacionado a él? (s|n) ");
		
		return in.nextLine().toUpperCase().equals("S");
	}
	
	/**
	 * Método encargado de mostrar el menú con las distintas funciones
	 * disponibles en la aplicación.
	 */
	private static void mostrarMenuFunciones() {
		System.out.println();
		System.out.println("########################");
		System.out.printf("#%22s#%n", "");
		System.out.println("#    FUNCIONES EXTRA   #");
		System.out.printf("#%22s#%n", "");
		System.out.println("########################");
		System.out.println();
		System.out.println("- Autor: César Ravelo Martínez\n");
		
		System.out.println("1) Insertar un equipo");
		System.out.println("2) Visualizar todos los contratos según un DNI o NIE");
		System.out.println("3) Visualizar la cantidad de futbolistas en activo");
		System.out.println("4) Visualizar la cantidad total de meses en activo de un jugador");
		System.out.println("0) Cancelar operación");
		System.out.print("\n> Introduzca la opción a ejecutar: ");
	}
	
	/**
	 * Método encargado de mostrar el listado de ligas.
	 * Delega la tarea de dar formato a la tabla a la función mostrarTablaDatos.
	 *
	 * @param listado ArrayList con los datos a mostrar
	 * @see mostrarTablaDatos
	 */
	private static void mostrarTablaContratos(ArrayList<Registro> listado) {
		mostrarTablaDatos(listado, new String[] {"id", "equipo", "nomLiga", "fechaInicio", "fechaFin", "precioAnual", "precioRecision"}, "%n%5s | %-40s | %-50s | %-11s | %-11s | %-11s | %-11s");
	}
	
	/**
	 * Método encargado de mostrar la tabla con los contratos activos.
	 * 
	 * @param _dato Array con los datos a mostrar; en el índice 0 el total general y en el 1 el total según criterio
	 */
	private static void mostrarTablaContratosActivos(int[] _dato) {
		System.out.println();
		if (_dato == null)
			System.out.println("No hay datos que mostrar.");
		else {
			System.out.println("# Cantidad de contratos en activos: " + _dato[0]);
			System.out.println("# Cantidad de contratos en activos con criterios dados: " + _dato[1]);
		}
	}
	
	/**
	 * Método encargado de mostrar el total de meses en activo de un jugador
	 * 
	 * @param totalMeses Total de meses activos
	 */
	private static void mostrarTotalMesesActivo(int totalMeses) {
		if (totalMeses == 0)
			System.out.println("# No ha estado ningún mes en activo");
		else
			System.out.println("# Total meses en activo: " + totalMeses);
	}

	/**
	 * Método encargado de iniciar la ejecución del menú.
	 */
	public static void iniciar() {
		try {
			Scanner in = new Scanner(System.in);
			int opc = -1;
			System.out.println("Antes de comenzar, es necesario especificar qué tipo de conexión se hará.");
			System.out.println("1) MySQL");
			System.out.println("2) SQLServer");
			System.out.println("3) Access");
			System.out.println("0) Salir\n");
			System.out.print("Indique qué opción le interesa: ");
			opc = in.nextInt();
			
			switch (opc) {
				case 1:
					Conexion.init("configMySQL.properties", Conexion.TIPO_CONEXION.MYSQL);					
					break;
				case 2:
					Conexion.init("configSQLServer.properties", Conexion.TIPO_CONEXION.SQLSERVER);
					break;
				case 3:
					Conexion.init("configAccess.properties", Conexion.TIPO_CONEXION.ACCESS);
					break;
				case 0:
					System.out.println("Se cerrará la aplicación sin problemas.");
					break;
			}
			
			if (opc > 0 && opc < 4)
				gestionarMenuInicial(in);
		} catch (IOException e) {
			System.err.println("La aplicación no se pudo iniciar. " + e.getLocalizedMessage());
		}
	}

}
