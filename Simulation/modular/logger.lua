
function logtimeopen(tstep,file,inittime)
	if (inittime == true) then
		local str = "{\"time\":"..tstep 
		file:write(str)
	 	inittime = false
	else
		local str = ",{\"time\":"..tstep 
		file:write(str)
	end
	return inittime
end

function logtimeclose(file)
	file:write("}")
end

function logSetP(Amp,Off,Ph,freq,file)

	local logtable = {
		amplitude = Amp,
		offset = Off,
		phasediff = {},
		v = freq,
	}

	for k in pairs(Ph) do logtable['phasediff'][k] = Ph[k] end
	--print('***********************')
	--for k in pairs(logtable) do print(logtable[k]) end
	local str = json.encode (logtable)
	file:write(",\"SetP\":"..str)
	--file:write("\n")
	--print(str)
end

function logCPG(teta,ampli,dampli,off,doff,file)
	local logtable = {
		teta = teta[1],
		amplitude = ampli,
		damplitude = dampli,
		offset = off,
		doffset = doff
	}

	local str = json.encode(logtable)
	file:write(",\"CPG\":"..str)
end


function logHorm(horm,file)

	local logtable ={}

	for i=1,#horm do
		logtable['h'..(i-1)] = horm[i]
	end
	
	
	--for k in pairs(horm) do logtable['hormones'][k] = horm[k] end
	--print('*************************')
	--for k,v in pairs(logtable) do print(k,v) end
	local str = json.encode(logtable)
	--print(str)

	file:write(",\"Horm\":"..str)
	--file:write("\n")

end

function logRecH(phorm,file)
	local receivtable={}
	for i=1,#phorm do
        	receivtable[i] = {}
    	end	
	--local proptable = {false,false,false,false}

	--print('*************************')
	local str = ""
	--for k,v in pairs(phorm) do print(k,v) end

	str = str..",\"HRec\":{"
	--print(str)

	str = str .."\"f1\":["
	for j = 1,#phorm[1] do 
		table.insert(receivtable[1],{false,false,false,false}) 
		local str2 = json.encode(simUnpackFloatTable(phorm[1][j]))
		if(j == 1) then
			str = str..str2
		else
			str = str..","..str2
		end
	end

	str = str.."]"
	
	for i =2, #phorm do
		local str1 = ",\"f"..i.."\":["
		--logtable['f'..(i)] = simUnpackFloatTable(phorm[i])
		--print(json.encode(simUnpackFloatTable(phorm[i])))
		--print('++++++++++++++++++++++++'..i)
		for j = 1,#phorm[i] do
			table.insert(receivtable[i],{false,false,false,false}) 
			local str2 = json.encode(simUnpackFloatTable(phorm[i][j]))
			if(j == 1) then
				str1 = str1..str2
			else
				str1 = str1..","..str2
			end
		end
		str1 = str1.."]"
		str = str .. str1
		--print(str1)
		
	end
	str = str.."}"
	--print(str)
	--for i=1,#receivtable do
		--print('++++++++++++++++++++++++'..i)
		--for j = 1,#receivtable[i] do
			--print('--------------------'..j)
			--for k,v in pairs(receivtable[i][j]) do print(k,v) end 
		--end
	--end
	
	file:write(str)

	return receivtable
	

end

function logProp(receivtable,file)
	--print('*************************')
	--for i=1,#receivtable do
		--print('++++++++++++++++++++++++'..i)
		--for j = 1,#receivtable[i] do
			--print('--------------------'..j)
			--for k,v in pairs(receivtable[i][j]) do print(k,v) end 
		--end
	--end

	local str = ""
	str = str..",\"HProp\":{"

	str = str .."\"f1\":["
	for j = 1,#receivtable[1] do 
		local str2 = json.encode(receivtable[1][j])
		if(j == 1) then
			str = str..str2
		else
			str = str..","..str2
		end
	end
	str = str.."]"

	for i = 2, #receivtable do
		local str1 = ",\"f"..i.."\":["
		for j = 1,#receivtable[i] do
			local str2 = json.encode(receivtable[i][j])
			if(j == 1) then
				str1 = str1..str2
			else
				str1 = str1..","..str2
			end
		end
		str1 = str1.."]"

		str = str .. str1		
	end
	str = str.."}"
	--print(str)
	file:write(str)
	

end

