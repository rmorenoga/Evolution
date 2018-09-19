package incremental;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import emst.evolution.haea.ModifiedHaeaStep;
import emst.evolution.json.haea.JSONHaeaStepObjectManager;
import emst.evolution.json.setting.EvolutionryAlgorithmSetting;
import emst.evolution.search.multithread.MultithreadOptimizationGoal;
import emst.evolution.search.population.PopulationDescriptors;
import emst.evolution.search.selection.ModifiedTournament;
import evolHAEA.EmP;
import evolHAEA.HyperCubeFromPoint;
import evolHAEA.PeriodicHAEAStep;
import evolHAEA.PeriodicOptimizationGoal;
import maze.Maze;
import simvrep.ShortChallengeSettings;
import simvrep.Simulation;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.HaeaStep;
import unalcol.evolution.haea.HaeaStepDescriptors;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperatorsDescriptor;
import unalcol.evolution.haea.WriteHaeaStep;
import unalcol.io.Write;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.PermutationPick;
import unalcol.optimization.real.mutation.PowerLawMutation;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.optimization.real.xover.SimpleXOver;
import unalcol.search.Goal;
import unalcol.search.RealQualityGoal;
import unalcol.search.population.IterativePopulationSearch;
import unalcol.search.population.Population;
import unalcol.search.population.PopulationSearch;
import unalcol.search.selection.Selection;
import unalcol.search.solution.Solution;
import unalcol.search.variation.Variation;
import unalcol.sort.Order;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.Tracer;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;
import util.ChromoConversion;

public class IncrementalShortChallenge {

	public static int Nsim;
	public static List<Simulation> simulators;

	public static void main(String[] args) {

		double fitness;
		double maxFitness = -0.5;
		int maxReplicas = 2;
		double[][] lastPop;

		launchSimulators(args);
		
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		double[] morphology = ChromoConversion.str2double(morpho);

		// Seed individual
		double[] seed = new double[] { -8.7022977429907, -2.6940289379121, -1.4431944620278, -1.3156390547095,
				-5.9917088877257, 2.1502158872271, -1.8173197715124, 6.3601008200855, -3.5660225156747, 5.5365650414796,
				4.5212175819355, -1.5748587388453, -1.8679579950777, -3.3668785635023, 2.7163317783425,
				-4.0101967559982, 0.44827410718419, -1.7826905127019, -0.65681582474724, 2.1614455769806,
				-1.5849860711937, 4.6201628870038, -8.4232000226166, 0.34810318480051, -1.9561337672583,
				2.1297740282082, -6.599877824989, -1.7311370083714, -0.30351509274486, -3.6028046113231,
				1.1641933966043, -0.15531190391452, 1.819400090726, 10.656706273988, -4.7216174713638, -1.7198508307612,
				-5.1067507578955, -0.75965039822201, -0.6579242546011, -7.3013308106715, -0.21916202633551,
				-0.74727257602751, -0.53323046645488, -2.1442090520183, 2.2553169249299, 1.0034260756077,
				7.4488926612386, -2.1539554315773, 2.8153406617647, -0.29526789914479, -1.0491002625198,
				-0.63245210557883, -2.9849698646478, -1.5463809407321, 0.96925360777549, -5.4092555565163,
				1.9643007245311, 1.0459478488144, -0.56989714410641, -5.8919576498618, 2.6547816271166,
				-4.0627558905375, 5.3785225012908, -1.6034413483164, -1.3444207394666, -1.938815350591, 4.8898545538278,
				-2.2853795958756, -4.5907575279811, -2.1747926125386, -3.1106557973604, -0.77782506746639,
				1.7396797732138, 2.011391695184, 0.6153368332229, 0.98637466757503, 3.2085267284138, 3.55296444308,
				-3.2946241076121, -1.7416794295971, -0.27879789327323, 3.0994469773162, -0.044354691587204,
				0.53502706021983, 0.84570892297645, -1.0197552885501, -0.39889241840827, 0.3620244886194,
				-0.12366261796141, 0.29646795489099, 2.3240841335079, -8.4793716791394, -1.5566841197772,
				-6.7924664007132, -1.9910793384311, -0.78604287753067, 0.94263425272582, 0.99455490139006,
				0.1831063108759, 10.90404258638, 10.744887188833, -4.9222827348566, -6.4798724096407, -5.6304014282439,
				-6.3528058457285, -2.208271088642, -3.7001251209713, 2.5781239043878, -2.9706634753782, 0.9786632379228,
				0.020026964292823, -5.4532939399025, 2.4832338183363, 5.5787478218772, -4.5145348942327,
				-2.1718640639422, -0.24742084042858, -6.5209491579998, 0.98340759484828, -3.3252754648605,
				-0.94841689635436, -4.2841796312746, -1.4250901527817, 1.5286600673457, 3.837692285624,
				-1.1024029727031, 5.8515496386829, -3.0508515848578, -2.8128737198484, -1.9431243554053,
				-2.5090990179585, -8.3949707093562, -0.67998410170728, 2.0720362235925, -0.79073010602328,
				2.2604178960305, 0.61465491232089, 2.3565744551852, 1.6467717459555, -0.73113096245264,
				0.18985241357151, 3.2528697425, 0.87636462277402, -11.021363851094, -5.7281075493773, 1.5184498545129,
				-4.840164666457, 1.3674077626239, -2.2987748018878, -1.4315083080383, -2.1543288290971,
				-0.34256357082856, 5.3855339468474, 13.93828812775, -0.97423073355602, -5.0715253305112,
				0.19062069143681, 0.39194993983837, -0.054760624972746, 0.17333144077449, -0.59174249319452,
				0.015125217539116, 2.1710706839209, -4.4253987585661, -0.16869808641266, 0.45463318262276,
				0.3808873668679, -1.7601356411731, -0.13205893642859, -5.9534729576773e-12, -8.9072011445436e-10,
				-4.5784812691234e-11, -1.5891738774221e-10, 6.0718920077286e-10, -4.9581008049421e-11,
				4.9755584480526e-12, 3.0461048315964e-10, 2.9626531267145e-10, -2.3323492308976e-09,
				-4.4026928010955e-10, -1.1423027652003e-10, 5.3121531421634e-11, 0.61903920840411, -2.5387545460449e-10,
				-1.8069363123008e-11, -4.4456626411208e-11, 1.7101606973857e-10, -1.5142168508576e-11,
				2.2987751203757e-12, 1.1293474376568e-10, 1.1529109609123e-10, -8.793904786674e-10,
				-1.3115399712235e-10, -4.238271594337e-11, 1.4848974383276e-11, -0.61903920840895, -3.405586182891e-10,
				-2.3207905052837e-11, -5.9813425351051e-11, 2.2984556514917e-10, -2.0079473115747e-11,
				2.8652693197513e-12, 1.4600689472198e-10, 1.4827024668214e-10, -1.1343333788331e-09,
				-1.7480851755986e-10, -5.4766160102315e-11, 1.9979975684791e-11, -0.28021913792796, 6.9958794431925,
				1.9149080083781, 1.6063659070415, -3.6176661260524, 1.318813811095, -0.23260515416759,
				-0.84301615625568, -4.6100931948043, 1.2200884419545, 3.143388151748, 0.47630009637218,
				-1.1064774052773, -0.16971508183716, -3.1912106322592, 2.2794946034352, -0.28934785714308,
				0.2129963491506, -0.26788281062592, -0.43967702357483, -2.0035689388687, -3.7853548813787,
				5.3783756402157, 3.0580856959208, 1.3283480946602, 0.73279123887078 };
		
		float[] times;
		float[] envFractions;
		List<Maze> mazeChallenges  = new ArrayList<Maze>();
		List<ShortChallengeSettings> challengeSettings = new ArrayList<ShortChallengeSettings>();
		List<double[][]> seeds = new ArrayList<double[][]>();
		
		
		/*Experiments*/
		
//		times = new float[]{2.5f,3.1f,5.23f,7.6f,10.9f,15.5f,18.2f,25.7f,30};//b
//		envFractions = new float[]{0.05f,0.1f,0.2f,0.35f,0.45f,0.55f,0.67f,0.8f,1};//b
//		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
//		mazeChallenges.add(new Maze(new char[] { 'b' }, 0.4f, 0.088f, 1));
//		seeds.add(new double[]{7.467041585510225,-0.430037643702913,-9.957454754495556,9.960528023820554,7.633914500985168,5.19379124039241,-1.333428575157539,-9.432854531689568,2.6574457439920205,-4.309516211556854,1.8463888569655782,-8.813172944489628,-5.933472237905358,-1.4901897065783323,-4.518306686089036,5.95661519922072,-2.4035934013137252,8.500015286485613,-3.4573198820688607,5.78521414139059,-1.9963490100680448,-9.36196008638463,2.830651885684638,4.672219520801658,2.3234906785763663,-9.150459348086045,-4.646968414848478,8.244757312344557,-6.581489537718417,-5.059099516895391,6.914004502239266,-4.5238342728026355,-7.096183319313077,-8.324275804789835,-9.589759935409177,4.797408871847626,3.1204079024204865,-7.689946876260669,5.310252530804921,5.874566596079421,-1.6502392279173772,-4.190959427390078,-7.639286761365764,9.670903709649233,-6.432083259923533,7.330937348887462,2.565656466667819,-5.266456020739715,7.836421373531678,2.332932875926418,-3.4496578534454847,2.956926718715954,-0.7585116645826268,9.947887440491456,-8.702362601703967,7.194696926470806,2.0830105551538103,-5.25732276214483,-9.336687596065135,-5.391490621434104,9.055041882830425,1.4639483430805071,1.3558247860637258,1.5257804166765097,1.949534828334015,4.71786843110353,5.883995466738748,7.316953170002346,6.615677444437764,6.044626680896303,4.567086003579581,6.437724117332422,-8.280177983422679,7.380902014059883,-6.4889365219548045,9.072323074359653,9.95975213107349,-8.463907363003043,5.204016157289064,7.718061302944725,0.5891212135663334,-1.0181716010100705,-6.5089860229788545,-3.979101791142925,4.544604483615833,-7.90700935560341,1.8190765422111894,5.145587880139666,6.960799321399393,-1.2344079133351533,0.8297335986553578,3.139258952168464,8.140075627966146,-2.97194345948158,2.64030426447089,-7.751652631418421,-0.2419585308517844,2.056549866732533,-5.010360678676349,2.305250357642482,8.208522554305691,-4.633998259797739,-1.3209281585822852,-5.342113518338899,-1.779536500028055,-0.6407759920591253,6.330335530483902,-8.267227835119177,-5.0799870007119,2.6145552495851114,-9.582045197837894,0.9619647476297569,2.7341149519377095,1.270236755850892,-9.600164669388516,1.6008325744981353,0.1743800013794781,2.253160734173824,-2.296092874397245,-2.5257025567562192,8.058498778025397,-7.055643992772923,-3.1777407607518176,-6.043098186971689,-5.056297926477644,-9.335371007172945,-6.76724436169611,-2.5504878432495843,-7.311711387615926,9.826108531870855,1.3213022551412197,-9.057802818605735,4.896108228648063,2.2348107794043877,-3.433084618040993,2.2693163729148287,-2.521788915776422,1.0891720676279333,2.134848571529544,7.141100636613387,-6.916573878286338,8.019901506581688,2.3776555380857767,3.367616082080508,-0.6610530863794128,-7.061321076456849,2.103903857543262,-9.402765520748103,9.390685246848406,-8.262269206569169,6.899047811014678,1.0245665258101397,-8.728667961016741,-6.412923589803682,-7.498107143184361,-4.027700952894063,-7.068891973286278,-3.024371896530178,-0.4531178570396647,9.076974078721005,6.870656517848275,-9.829215846507248,-5.532159750239485,0.7365129699301931,-2.507113289303286,3.5300753564496783,-7.472246535312901,6.38202093717263,-8.260338517482541,-6.757570663961437,-2.310946763365848,2.582632027313587,5.53707798084275,-4.38959928958254,6.174363644251445,-6.665842770216969,6.9312629657992355,2.948653356472053,3.239616805112737,-9.816390760182252,9.683470962113596,2.4657906212774745,-8.90317090809413,4.519469417219398,-8.18884296892461,7.983483078290912,-0.19597080596554894,-4.749688317431089,-0.5515680177638014,3.2875848813344684,-8.99979638503857,-7.199828243998956,2.9813163569935384,5.889939074278501,-9.81854560080216,2.5447158532073164,8.626510006609902,-7.524098294368407,4.089028684099955,0.7106291359848184,1.5606077108164633,3.658868369466924,8.495884986279851,-9.344443966429816,9.658561660138856,-7.230747469170203,-0.05148486409984958,-3.14857507473221,5.064467607167379,7.159773431453397,-6.084163589535001,6.825057759878022,1.039554955385335,-4.684736414877268,-0.4567308392023581,-1.5620570267782228,-8.197815187811747,-9.848083915786457,-1.4302467481500936,9.256878249666869,8.68019629431156,3.829030839009134,1.1787958901696804,7.17664148722045,-2.28170999767037,-5.568875132335822,1.4801730990172652,4.450120704628042,-7.741162140348407,9.231571612759085,5.808740336554125,-4.188315357106609,-9.247338201251651,-3.7761638969405973});
//		
//		times = new float[]{2.5f,3.1f,5.23f,7.6f,10.9f,15.5f,18.2f,25.7f,30};//b
//		envFractions = new float[]{0.05f,0.1f,0.2f,0.35f,0.45f,0.55f,0.67f,0.8f,1};//b
//		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
//		mazeChallenges.add(new Maze(new char[] { 'b' }, 0.4f, 0.088f, 1));
//		seeds.add(new double[]{6.8747064381162755,-0.962113617479919,-9.84705451820734,9.672883326047462,7.630919795164587,4.884115107051219,-1.0660008126068337,-9.390425707599196,2.7508343450730672,-3.84322985908952,2.035676285115252,-8.524009089740481,-5.870100388444882,-1.8112995388754807,-4.660715138204075,6.077922342162928,-2.333348205538676,8.66233568769696,-9.907344234016943,5.720636039698501,-1.6480046534570092,-10.0,2.287154049997739,5.144819008332933,2.239021548256819,-9.019843788522364,-4.908579234058951,8.92733433038666,-7.032120968092109,-5.089437530426872,6.494978257336717,-5.200771531151387,-7.204118440316757,-8.547328062482624,-9.785520225694572,4.849908760319373,3.0725131633055742,-7.817376782854333,5.290389169480278,5.6398015785721025,-1.327366815561501,-4.289137499622898,-7.725692329966449,9.778223363594833,-7.201369369377378,7.039753580106318,2.2803030546968586,-5.392783824928933,7.241796404677045,2.6436934930645233,-3.005822712061729,2.7666633384299044,-1.0177771543368128,9.865498606869274,-8.31950112344824,7.222244469919336,2.313119413785851,-5.290552078515427,-9.414532337680262,-5.306488755766933,9.233873917451115,3.8091845015594274,0.8426315584206745,1.8658254955334381,2.417214998295351,4.811269221328948,6.281821601943031,7.3232565844414985,6.827343650679042,5.796728044338733,5.330901218272068,6.744825482588619,-9.403587564002025,7.587855656991652,-6.211785156663286,8.644322393472663,9.955179716328251,-9.76331675351274,5.569453324443857,7.384712064004328,0.36225178901435134,-0.9612779274331156,-0.9383476702288143,-3.822042195661303,4.396741678301545,-8.351667504545768,1.9176547954476955,5.124512525759648,6.604278805831807,-0.8163879828619783,0.5304709925233999,3.211828421728113,8.333763873884905,-3.132920357970555,2.714093865330206,-7.646439947610569,0.07360618757065805,2.1232369513421525,-5.09608229013224,1.5691313532377722,8.634334070654743,-4.569490601164889,-1.3532067696957824,7.6173969359559575,-1.8851444039169887,-1.1295263304702465,6.310890263593277,-8.19231289209218,-5.389361973981833,2.8167692119122876,-9.25918171289282,0.8011200583509552,2.8205159193865796,1.366116607017159,-9.761137068236428,1.4661130122531825,0.2169683499078328,2.6364668666600877,-2.344477487468319,-2.453865507065972,8.381274430365849,-7.138005061862628,-3.7176914013157827,-5.744927525584394,-4.748683099815771,-8.849800468877564,-6.760824441780773,-2.127827684504562,-7.082917568784997,9.884967443427282,2.737486384727303,-9.402155342561224,4.603327191130934,-0.6277716304271146,-3.5810495995310236,1.5627092517012273,-1.9923081218149845,1.0825640932740153,0.391109073349578,7.4927983819173445,-6.652960144131749,7.897019503256258,2.7290178697740806,3.3720982604209215,-1.165643356289935,-6.716936186411186,1.8692344793523903,-9.593327728093778,9.383920810770721,-8.579447286475434,7.197851303206743,1.215750182028743,-8.26460908379135,-6.640198414065349,-7.1774817845324215,-3.8553026132901476,-6.83646619167931,-3.0749833901169916,-0.19280012776071132,9.114966254587193,7.038103439798662,-9.979457645277758,-5.339194565534711,1.2329058292664095,0.5622035899466965,3.5520741622122376,-7.690639481830606,9.020056746848157,-8.059672777748025,-7.02734652855545,-2.686849463033476,2.3539885684450543,5.458285429852606,-4.386825774491312,6.544619939080578,-6.110551866262118,6.766446326355993,2.9921630399556207,3.3741779130475544,-9.442835488583793,9.382249505761227,2.1042812600937033,-8.764935744827858,4.5674808266539975,-8.24188448932886,7.760484105748226,0.09402426227607462,-5.139684248163525,0.6211700874971798,7.716318162901079,-9.303133050507011,-7.129420114740238,3.071406479760042,5.8852836516171,-9.879151919677325,2.4485112193085374,8.878945938092963,-7.689565309836809,4.375581741057901,-0.6859679382251052,1.3911518135728973,4.035641009818676,8.557245298468269,-9.263356600923807,9.888767656604154,-7.097271312145701,-0.30871342934288415,-3.22010892798405,5.003212973180575,7.7887193705464846,-6.154512963037087,7.1499958761591085,1.7343894702403224,-4.563673961990286,-0.06607252128022649,-1.2178302115353277,-7.925210271622099,-9.918837877107304,-1.0948666145859545,8.8060933149677,8.288430972153936,3.613459415367668,1.162369661150804,9.889055708194196,-2.4571215620043616,-5.899640574849367,1.5729774516936772,4.730436359795507,-8.10128468003915,9.641140867277667,6.332223216514359,-3.85242122982665,-9.331685255362554,-6.008325668303888});
//		
//		
		/* Experiment 1 */
//		times = new float[] { 2.5f,5.23f,12.22f,15,16.3f,21.2f,23.4f,30};
//		envFractions = new float[] {0.05f,0.17f,0.45f,0.5f,0.55f,0.67f,0.75f,1};
//		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
//		mazeChallenges.add(new Maze(new char[] { 'l' }, 0.4f, 0.088f, 1));
//		seeds.add(null);
		
		times = new float[] { 2.5f,5.23f,8.6f,10.2f,11.9f,13.9f,15.1f,16.6f,18.3f,20.5f,21.5f,23.4f,30};
		envFractions = new float[] {0.05f,0.17f,0.28f,0.33f,0.37f,0.45f,0.5f,0.55f,0.6f,0.67f,0.7f,0.75f,1};
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'b' }, 0.4f, 0.088f, 3));
		seeds.add(null);
		
		
		/* Experiment 2 */
//		times = new float[] { 2.5f,5.23f,12.22f,15,16.3f,21.2f,23.4f,30};
//		envFractions = new float[] {0.05f,0.17f,0.45f,0.5f,0.55f,0.67f,0.75f,1};
//		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
//		mazeChallenges.add(new Maze(new char[] { 'r' }, 0.4f, 0.088f, 1));
//		seeds.add(null);
		
		times = new float[] { 2.5f,5.23f,7.5f,8.9f,12.22f,15.1f,16.6f,20.9f,21.5f,23,25,30};
		envFractions = new float[] {0.05f,0.17f,0.25f,0.29f,0.45f,0.5f,0.55f,0.67f,0.71f,0.75f,0.78f,1};
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'b' }, 0.4f, 0.088f, 4));
		seeds.add(null);
		
		

		/*// Define challenges
		//float[] times = new float[] { 1.5f, 5.234f, 11.2285f, 19.4833f, 30 }; //Turns
		//float[]  times = new float[]{1.5f,3.1f,5.23f,7.6f,10.9f,15.5f,18.2f,25.7f,30};//b
		//float[] times = new float[]{1.5f,5.23f,11.22f,12.7f,14f,18.2f,23.4f,30};//sb
		float[] times = new float[]{2.5f,3.1f,5.23f,7.6f,10.9f,15.5f,18.2f,25.7f,30};//s
		
		
		//float[] envFractions = new float[] { 0.05f, 0.1744f, 0.4574f, 0.7575f, 1 };//Turns
		//float[] envFractions = new float[]{0.05f,0.1f,0.2f,0.35f,0.45f,0.55f,0.67f,0.8f,1};//b
		//float[] envFractions = new float[]{0.05f,0.17f,0.45f,0.5f,0.55f,0.67f,0.75f,1};//sb
		float[] envFractions = new float[]{0.05f,0.085f,0.18f,0.35f,0.45f,0.55f,0.67f,0.8f,1};//s
		ShortChallengeSettings settings = new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,
				false);
		Maze maze = new Maze(new char[] { 's' }, 0.4f, 0.088f, 1);*/
		
		ShortChallengeSettings settings;
		Maze maze;
		
		for (int challenge = 0;challenge < challengeSettings.size();challenge++){
			
			settings = challengeSettings.get(challenge);
			maze = mazeChallenges.get(challenge);
			lastPop = seeds.get(challenge);

			for (int repli = 0; repli < maxReplicas; repli++) {
				
				settings.selectChallenge(0);

				JSONObject challengeResult = new JSONObject();
				
				JSONObject test = new JSONObject();
				test.put("Name", "IncrF"+new String(maze.structure)+challenge+repli);
				test.put("times", settings.getTimes());
				test.put("envFractions", settings.getFractions());
				test.put("noisy", settings.noisy);
				test.put("individualParamters", settings.individualParameters);
				test.put("maze", maze.structure);
				test.put("width", maze.width);
				test.put("height", maze.height);
				test.put("steps", maze.nBSteps);
				test.put("maxFitness", maxFitness);
				
				challengeResult.put("test", test);
				

				for (int i = 0; i < envFractions.length; i++) {

					JSONObject challengeStep = new JSONObject();

					if (lastPop != null) {
						simulators = new ArrayList<Simulation>();
						connectToSimulator(0);
						EmP function = new EmP(simulators, 1, morphology, maze, settings);
						fitness = function.apply(lastPop[0]);
						simulators.get(0).Disconnect();
						challengeStep.put("lastBest", lastPop[0]);
						challengeStep.put("fitness", fitness);
					} else {
						fitness = 1.0;
						challengeStep.put("lastBest", -1);
						challengeStep.put("fitness", fitness);
					}

					System.out.println("Fitness = " + fitness + ", Challenge: " + settings.getSelection());

					if (fitness > maxFitness) {
						try {
							Solution<double[]>[] bestPop = evolve(morphology, maze, settings, 100, 30,
									maxFitness,(String) test.get("Name"), lastPop);
							
							lastPop = new double[bestPop.length][];
							for(int j = 0; j < lastPop.length; j++)
								lastPop[j] = bestPop[j].object();
							fitness = (double) bestPop[0].info(Goal.GOAL_TEST);

							challengeStep.put("lastBestEvol", lastPop[0]);
							challengeStep.put("lastPopEvol", lastPop);
							challengeStep.put("fitnessEvol", fitness);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						challengeStep.put("lastBestEvol", -1);
						challengeStep.put("fitnessEvol", -1);
					}

					settings.selectNextChallenge();
					challengeResult.put("challenge" + i, challengeStep);
				}

				try (Writer writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(test.get("Name")+".json"), "utf-8"))) {
					writer.write(challengeResult.toString());
				} catch (Exception e) {

				}

			}
		}
		
		stopSimulators();
		//System.out.println(challengeResult);
	}

	static Solution<double[]>[] evolve(double[] morphology, Maze maze, ShortChallengeSettings settings,int POPSIZE,int MAXITERS, double maxFitness,String name, double[][] lastPop)
		throws IOException { // Must test for fitness and max iterations finishing conditions

		simulators = new ArrayList<Simulation>();
		for (int j = 0; j < Nsim; j++) {
			connectToSimulator(j);
		}
		
		int realDIM = 234;
		double[] min = DoubleArray.create(realDIM, -10);
		double[] max = DoubleArray.create(realDIM, 10);
		
		HyperCube realSpace;
		if (lastPop!= null){
		double[][] referencePoints = lastPop; 
		double[] radius = DoubleArray.create(realDIM, 0.2);//Can this be done without problems?
		realSpace = new HyperCubeFromPoint(min, max, referencePoints, radius);
		}else{
			realSpace = new HyperCube(min, max);
		}
		
		
		
		//HyperCube realSpace = new HyperCube(min, max);
		

		OptimizationFunction<double[]> function = new EmP(simulators, Nsim, morphology, maze, settings);
		MultithreadOptimizationGoal<double[]> goal = new PeriodicOptimizationGoal<double[]>(function);
		goal.setMax_threads(Nsim);

		IntensityMutation realVariation = new PowerLawMutation(0.2, new PermutationPick(23));
		LinearXOver realXOver = new LinearXOver(); // Use Tournament(4)
		SimpleXOver simpleXOver = new SimpleXOver();

		//int POPSIZE = 30;
		//int MAXITERS = 100;
		Variation[] opers = new Variation[3];
		opers[0] = realVariation;
		opers[1] = realXOver;
		opers[2] = simpleXOver;

		SimpleHaeaOperators operators = new SimpleHaeaOperators(opers);
		Selection selection = new ModifiedTournament(4);

		// ModifiedHaeaStep step = new ModifiedHaeaStep(POPSIZE, selection, operators);
		ModifiedHaeaStep step = new PeriodicHAEAStep(POPSIZE, selection, operators);
		step.setJsonManager(new JSONHaeaStepObjectManager());
		PopulationSearch search = new IterativePopulationSearch(step, new ForLoopFitnessCondition(MAXITERS, maxFitness));

		// Track Individuals and Goal Evaluations
		WriteDescriptors write_desc = new WriteDescriptors();
		Descriptors.set(Population.class, new PopulationDescriptors());
		Descriptors.set(HaeaStep.class, new HaeaStepDescriptors());
		Descriptors.set(HaeaOperators.class, new SimpleHaeaOperatorsDescriptor());
		Write.set(double[].class, new DoubleArrayPlainWrite(false));
		Write.set(HaeaStep.class, new WriteHaeaStep());
		// Descriptors.set(Population.class, new
		// PopulationDescriptors());
		Write.set(Population.class, write_desc);
		Write.set(HaeaStep.class, write_desc);
		Write.set(HaeaOperators.class, write_desc);

		// Add tracer based on descriptors set
		// FileTracer tracer = new FileTracer("Evolresult.txt", ',');
		ConsoleTracer tracer1 = new ConsoleTracer();
		Tracer.addTracer(search, tracer1);

		EvolutionryAlgorithmSetting easetting = new EvolutionryAlgorithmSetting(name +"evol"+ settings.getSelection(), POPSIZE,
				MAXITERS);

		Solution<double[]> solution = search.solve(realSpace, goal);
		Population<Solution<double[]>> pop = ((PeriodicHAEAStep)step).lastPop;
		 
		Solution<double[]>[] spop = new Solution[pop.size()];
		int[] indexes = new int[spop.length];
		for(int i = 0; i < spop.length; i++) {
			spop[i] = pop.object()[i].object();
			indexes[i] = i;
		}
    	Double[] popFitness = goal.apply(spop);
    	sort(indexes, popFitness, goal.order());
    	
    	Solution<double[]>[] bestpop = new Solution[(int)(0.1 * spop.length)];
    	for(int i = 0; i < bestpop.length; i++)
    		bestpop[i] = spop[indexes[i]];

		System.out.println(solution.object());

		tracer1.close();
		
		JSONObject result = new JSONObject();
		result.put("settings", easetting.encode());
		result.put("evolution", step.getJsonManager().encode());
		JSONObject jsonsolution = new JSONObject();
		jsonsolution.put("best_individual", solution.object());
		jsonsolution.put("best_fitness", solution.info(Goal.GOAL_TEST));
		result.put("solution", jsonsolution);
		String path = "./";
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path + easetting.title + ".json"), "utf-8"))) {
			writer.write(result.toString());
		} catch (Exception e) {

		}

		for (Simulation sim : simulators) {
			sim.Disconnect();
		}
		
		return bestpop;

	}

	private static void sort(int[] indexes, Double[] fitness, Order<Double> order) {
		quicksort(indexes, fitness, order, 0, indexes.length - 1);
	}

	private static void quicksort(int[] indexes, Double[] fitness, Order<Double> order, int lo, int hi) {
		if (lo < hi) {
			int p = partition(indexes, fitness, order, lo, hi);
			quicksort(indexes,fitness,order,lo,p-1);
			quicksort(indexes,fitness, order, p+1,hi);
		}
		
	}

	private static int partition(int[] indexes, Double[] fitness, Order<Double> order, int lo, int hi) {
		double pivot = fitness[indexes[hi]];
		int i = lo;
		int temp;
		for (int j = lo; j<hi;j++) {
			if (order.compare(fitness[indexes[j]], pivot) > 0) {
				temp = indexes[i];
				indexes[i] = indexes[j];
				indexes[j] = temp;
				i = i + 1;
			}
		}
		temp = indexes[i];
		indexes[i] = indexes[hi];
		indexes[hi] = temp;
		return i;
	}

	static void connectToSimulator(int simulatorNumber) {
		Simulation sim = new Simulation(simulatorNumber);
		// Retry if there is a simulator crash
		for (int i = 0; i < 5; i++) {
			if (sim.Connect()) {
				simulators.add(sim);
			} else {
				// No connection could be established
				System.out.println("Failed connecting to remote API server");
				System.out.println("Trying again for the " + i + " time in " + simulatorNumber);
				continue;
			}
			break;
		}
	}

	static void stopSimulators() {
		
		// Stop Simulators
		for (int j = 0; j < Nsim; j++) {
			// kill all the v-rep processes
			try {
				ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + j);
				File log = new File("Simout/log");
				qq.redirectErrorStream(true);
				qq.redirectOutput(Redirect.appendTo(log));
				Process p = qq.start();
				int exitVal = p.waitFor();
				System.out.println("Terminated vrep" + j + " with error code " + exitVal);
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
		
	}
	
	static void launchSimulators(String[] args) {
		Nsim = 0;

		if (args.length > 0) {
			try {
				// for(int j=0;j<args.length;j++){
				// System.out.println("Argument "+j+" = "+args[j]);
				// }
				if (args.length >= 1) {
					Nsim = Integer.parseInt(args[0]);
				} else {
					System.err.println("Provide a number of simulators");
					System.exit(1);
				}
			} catch (NumberFormatException e) {
				System.err.println("Nsim " + args[0] + " must be an integer.");
				System.exit(1);
			}
		} else {
			System.err.println("Missing arguments");
			System.exit(1);
		}

		
		System.out.println(Nsim);
		// Start Simulators
		for (int j = 0; j < Nsim; j++) {

			String vrepcommand = new String("./vrep" + j + ".sh");

			// Initialize a v-rep simulator based on the Nsim parameter
			try {
				// ProcessBuilder qq = new ProcessBuilder(vrepcommand,
				// "-h",
				// "scenes/Maze/MRun.ttt"); //Snake
				// ProcessBuilder qq = new ProcessBuilder("xvfb-run","-a",vrepcommand, "-h",
				// "scenes/Maze/defaultmhs.ttt");
				ProcessBuilder qq = new ProcessBuilder(vrepcommand,"-h", "scenes/Maze/defaultmhs.ttt");
				qq.directory(new File("/home/rodr/V-REP/Vrep" + j + "/"));
				File log = new File("Simout/log");
				qq.redirectErrorStream(true);
				qq.redirectOutput(Redirect.appendTo(log));
				qq.start();
				Thread.sleep(10000);
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}

	}
}
