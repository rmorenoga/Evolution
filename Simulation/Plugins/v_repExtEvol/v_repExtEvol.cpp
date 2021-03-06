// Author: Rodrigo Moreno 
// rmorenoga@unal.edu.co
// www.alife.unal.edu.co
// 
// -------------------------------------------------------------------
// THIS FILE IS DISTRIBUTED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
// WARRANTY. THE USER WILL USE IT AT HIS/HER OWN RISK. THE ORIGINAL
// AUTHORS WILL NOT BE LIABLE FOR DATA LOSS,
// DAMAGES, LOSS OF PROFITS OR ANY OTHER KIND OF LOSS WHILE USING OR
// MISUSING THIS SOFTWARE.
// 
// You are free to use/modify/distribute this file for whatever purpose!
// -------------------------------------------------------------------
//
// 

#include "v_repExtEvol.h"
#include "scriptFunctionData.h"
#include <iostream>
#include "v_repLib.h"
#include <cstdlib>


#ifdef _WIN32
	#ifdef QT_COMPIL
		#include <direct.h>
	#else
		#include <shlwapi.h>
		#pragma comment(lib, "Shlwapi.lib")
	#endif
#endif /* _WIN32 */
#if defined (__linux) || defined (__APPLE__)
	#include <unistd.h>
	#define WIN_AFX_MANAGE_STATE
#endif /* __linux || __APPLE__ */

#define CONCAT(x,y,z) x y z
#define strConCat(x,y,z)	CONCAT(x,y,z)

#define PLUGIN_NAME "Evol"
#define PLUGIN_VERSION 1

LIBRARY vrepLib; // the V-REP library that we will dynamically load and bind

std::vector<float> genind(int indsize)
{
	std::vector<float>  genotype;
	for (int i=0;i<indsize;i++)
	{
		float gene = -2 + static_cast <float> (rand()) /( static_cast <float> (RAND_MAX/(4)));
		genotype.push_back(gene);
	}
	return genotype;
}	
		

// --------------------------------------------------------------------------------------
// simExtEvol_genpop
// --------------------------------------------------------------------------------------

#define LUA_GENPOP_COMMAND "simExtEvol_genpop" // the name of the new Lua command

const int inArgs_GENPOP[]={ // Decide what kind of arguments we need
	2, // we want 2 input arguments
	sim_script_arg_int32,0, // first argument is an integer
	sim_script_arg_int32,0, // second argument is an integer
};

void LUA_GENPOP_CALLBACK(SScriptCallBack* cb)
{ // the callback function of the new Lua command ("simExtEvol_genpop")
	CScriptFunctionData D;
	// If successful the command will return an integer (result), and a table of tables (individuals, which are float tables). If the command is not successful, it will not return anything
	bool success=false;
	int returnResult = 0;
	//std::vector<std::vector<float> > population;
	std::vector<float> randvector;
	if (D.readDataFromStack(cb->stackID,inArgs_GENPOP,inArgs_GENPOP[0],LUA_GENPOP_COMMAND))
	{ // above function reads in the expected arguments. If the arguments are wrong, it returns false and outputs a message to the simulation status bar
		std::vector<CScriptFunctionDataItem>* inData=D.getInDataPtr();
		int popsize =inData->at(0).int32Data[0]; // the first argument
		int indsize =inData->at(1).int32Data[0]; // the second argument
		
		for (int i=0;i<popsize;i++)
		{
			//population.push_back(genind(indsize));
			float gene = -2 + static_cast <float> (rand()) /( static_cast <float> (RAND_MAX/(4)));
			randvector.push_back(gene);
		}
		success = true;
		returnResult=1;
	}
	else
		simSetLastError(LUA_GENPOP_COMMAND,"Invalid arguments."); // output an error message to the simulator's status bar

	if (success)
	{ // prepare the return values:
		D.pushOutData(CScriptFunctionDataItem(returnResult));
		D.pushOutData(CScriptFunctionDataItem(randvector));
	}
	D.writeDataToStack(cb->stackID);
}


// --------------------------------------------------------------------------------------

// This is the plugin start routine (called just once, just after the plugin was loaded):
VREP_DLLEXPORT unsigned char v_repStart(void* reservedPointer,int reservedInt)
{
	// Dynamically load and bind V-REP functions:
	// ******************************************
	// 1. Figure out this plugin's directory:
	char curDirAndFile[1024];

#ifdef _WIN32
	#ifdef QT_COMPIL
		_getcwd(curDirAndFile, sizeof(curDirAndFile));
	#else
		GetModuleFileName(NULL,curDirAndFile,1023);
		PathRemoveFileSpec(curDirAndFile);
	#endif
#elif defined (__linux) || defined (__APPLE__)
	getcwd(curDirAndFile, sizeof(curDirAndFile));
#endif

	std::string currentDirAndPath(curDirAndFile);

	// 2. Append the V-REP library's name:
	std::string temp(currentDirAndPath);
#ifdef _WIN32
	temp+="\\v_rep.dll";
#elif defined (__linux)
	temp+="/libv_rep.so";
#elif defined (__APPLE__)
	temp+="/libv_rep.dylib";
#endif /* __linux || __APPLE__ */

// 3. Load the V-REP library:
	vrepLib=loadVrepLibrary(temp.c_str());
	if (vrepLib==NULL)
	{
		std::cout << "Error, could not find or correctly load the V-REP library. Cannot start 'Evol' plugin.\n";
		return(0); // Means error, V-REP will unload this plugin
	}
	if (getVrepProcAddresses(vrepLib)==0)
	{
		std::cout << "Error, could not find all required functions in the V-REP library. Cannot start 'Evol' plugin.\n";
		unloadVrepLibrary(vrepLib);
		return(0); // Means error, V-REP will unload this plugin
	}

	// ******************************************

	// Check the version of V-REP:
	// ******************************************
	int vrepVer;
	simGetIntegerParameter(sim_intparam_program_version,&vrepVer);
	if (vrepVer<30200) // if V-REP version is smaller than 3.02.00
	{
		std::cout << "Sorry, your V-REP copy is somewhat old. Cannot start 'Evol' plugin.\n";
		unloadVrepLibrary(vrepLib);
		return(0); // Means error, V-REP will unload this plugin
	}
	// ******************************************
	
	// Register the new Lua command "simExtEvol_genpop":
	simRegisterScriptCallbackFunction(strConCat(LUA_GENPOP_COMMAND,"@",PLUGIN_NAME),strConCat("number result, table_n randvector=",LUA_GENPOP_COMMAND,"(number popsize,number indsize)"),LUA_GENPOP_CALLBACK);

	return(PLUGIN_VERSION); // initialization went fine, we return the version number of this plugin (can be queried with simGetModuleName)
}

// This is the plugin end routine (called just once, when V-REP is ending, i.e. releasing this plugin):
VREP_DLLEXPORT void v_repEnd()
{
	// Here you could handle various clean-up tasks

	unloadVrepLibrary(vrepLib); // release the library
}

// This is the plugin messaging routine (i.e. V-REP calls this function very often, with various messages):
VREP_DLLEXPORT void* v_repMessage(int message,int* auxiliaryData,void* customData,int* replyData)
{ // This is called quite often. Just watch out for messages/events you want to handle
	// Keep following 4 lines at the beginning and unchanged:
	int errorModeSaved;
	simGetIntegerParameter(sim_intparam_error_report_mode,&errorModeSaved);
	simSetIntegerParameter(sim_intparam_error_report_mode,sim_api_errormessage_ignore);
	void* retVal=NULL;

// Keep following unchanged:
	simSetIntegerParameter(sim_intparam_error_report_mode,errorModeSaved); // restore previous settings
	return(retVal);
}



