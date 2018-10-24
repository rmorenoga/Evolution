
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

function logRecH(rhorm,file)

	
	local str = ""
	str = str..",\"HRec\":{"

	str = str .."\"f1\":["

	for j = 1,#rhorm[1] do 
		local str2 = json.encode(simUnpackFloatTable(rhorm[1][j]))
		if(j == 1) then
			str = str..str2
		else
			str = str..","..str2
		end
	end

	str = str.."]"	

	for i =2, #rhorm do
		local str1 = ",\"f"..i.."\":["
		for j = 1,#rhorm[i] do
			local str2 = json.encode(simUnpackFloatTable(rhorm[i][j]))
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
	
	file:write(str)			

end

function logRecHTr(rhormtr,file)

	local str = ""
	str = str..",\"HRecTr\":{"

	str = str .."\"f1\":["
	for j = 1,#rhormtr[1] do 
		local str2 = json.encode(simUnpackFloatTable(rhormtr[1][j]))
		if(j == 1) then
			str = str..str2
		else
			str = str..","..str2
		end
	end

	str = str.."]"	

	for i =2, #rhormtr do
		local str1 = ",\"f"..i.."\":["
		for j = 1,#rhormtr[i] do
			local str2 = json.encode(simUnpackFloatTable(rhormtr[i][j]))
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
	file:write(str)	

end

function logOri(sensorO,file)
	
	local ori = orientation(sensorO)

	local str = ""
	str = str..",\"Ori\":"..ori
	file:write(str)	

	
end

function logProp(active,connh,rhormnew,file)

	local receivtable = {}
	for i=1,#active do
		receivtable[i]={}
	end


	for i=1,#active do
		for j=1,#active[i] do
			table.insert(receivtable[i],{false,false,false,false}) 
		end
	end

	for i=1,#connh do
		if (connh[i]~=-1) then
			for j=1,#connh do
				if (j~=i and connh[j]~=-1 and #active[j]>0) then
					for k=1,#active[j] do
						receivtable[j][k][i] = active[j][k]
					end
				end
			end
		end
	end
	

	local str = ""
	str = str..",\"HPropTable\":{"

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
	
	str = ""
	str = str..",\"HPropActive\":{"

	str = str .."\"f1\":["
	for j = 1,#active[1] do 
		local str2 = active[1][j]
		if(j == 1) then
			str = str..str2
		else
			str = str..","..str2
		end
	end

	str = str.."]"	

	for i =2, #active do
		local str1 = ",\"f"..i.."\":["
		for j = 1,#active[i] do
			local str2 = active[i][j]
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
	file:write(str)	

	str = ""
	str = str..",\"HProp\":{"

	str = str .."\"f1\":["
	for j = 1,#active[1] do 
		local str2 = json.encode(simUnpackFloatTable(rhormnew[1][j]))
		if(j == 1) then
			str = str..str2
		else
			str = str..","..str2
		end
	end

	str = str.."]"	

	for i =2, #active do
		local str1 = ",\"f"..i.."\":["
		for j = 1,#active[i] do
			local str2 = json.encode(simUnpackFloatTable(rhormnew[i][j]))
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
	file:write(str)	

end

function logHCount(count,file)
	local logtable ={}

	for i=1,#count do
		logtable['h'..(i-1)] = count[i]
	end

	local str = json.encode(logtable)
	--print(str)

	file:write(",\"HCount\":"..str)
end


function logHCountE(count,file)
	local logtable ={}

	for i=1,#count do
		logtable['h'..(i-1)] = count[i]
	end

	local str = json.encode(logtable)
	--print(str)

	file:write(",\"HCountE\":"..str)
end

