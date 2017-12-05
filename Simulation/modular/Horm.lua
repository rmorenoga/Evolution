require('lua/modular/GenHormone')
require('lua/modular/PropagateH')
require('lua/modular/ReceptorsH')
require('lua/modular/Spatial')


function ghormone(connh,sensorR,sensorD,sensorO,Genmodel)
	local hormones = {}
	local sendhorm = false
	local ori = 0

	--Returns the generated hormone based on the activated sensors, the orientation of the model: sensorO
	-- and according to the the GenModel
	ori = orientation(sensorO)

	if (Genmodel=='baseHormone') then
		baseprob = 0.75
		hormones,sendhorm = ghormonebase(connh,sensorR,sensorD,baseprob,ori)
	else
		print('General Hormone Generation Model is not recognized')
	end

	return hormones,sendhorm
end

function receptors(hormones,rhorm,sensorO,connori,ampd,offd,phasediff,v,deltaparam,RecModel,Genmodel)
	local hormnew = {}
	local ampdnew = ampd
	local offdnew = offd
	local phasediffnew = {}
	for j=1,#phasediff do
        phasediffnew[j] = phasediff[j]
    end
	local vnew = v
	local ori = 0

	--Must update the CPG parameters by acting on the generated hormone: hormones and on the received hormones: rhorm
	--taking into account the connori orientations and the orientation of the module
	--Modifies the generated hormone according to the GenModel

	--Apply spatial transformation to incoming messages and extract orientation information before applying receptors

	ori = orientation(sensorO)


	if(Recmodel == 'BasicSum') then
		local delta = 0.01
		for i=1,#hormones do
			hormnew[i] = hormones[i]
		end
		
		ampdnew,offdnew,phasediffnew,vnew = receptorsbase(hormnew,ampdnew,offdnew,phasediffnew,vnew,deltaparam,delta)
		
		if (Genmodel=='baseHormone') then
			hormnew[1]=-1
		end

		for i=1,#rhorm do
			if (#rhorm[i] > 0) then
				for j=1,#rhorm[i] do
					local exhorm = simUnpackFloatTable(rhorm[i][j])
					--local exthorm = baseHsptransform(exhorm,i,connori[i])
					ampdnew,offdnew,phasediffnew,vnew = receptorsbase(exhorm,ampdnew,offdnew,phasediffnew,vnew,deltaparam,delta)	
				end
			end
		end


	elseif (Recmodel == 'ANNBasic') then
		for i=1,#hormones do
			hormnew[i] = hormones[i]
		end
		
		local hormsum = normalizedHSum(hormones,rhorm)

		--print('******************************************')
		--print(#deltaparam)
		--print(ori)
		--for k,v in pairs(hormsum) do print(k,v) end

		if (Genmodel=='baseHormone') then
			hormnew[1]=-1
		end

		ampdnew,offdnew,phasediffnew,vnew = receptorsANNB(hormsum,ampdnew,offdnew,phasediffnew,vnew,ori,deltaparam)
		--ampdnew,offdnew,phasediffnew,vnew = receptorsANNLastTime(hormsum,ampdnew,offdnew,phasediffnew,vnew,ori,deltaparam)

	else

		print('General Hormone Reception Model is not recognized')
	end
	

	return hormnew,ampdnew,offdnew,phasediffnew,vnew
end

function propagate(rhorm,connh,Propmodel,Direction)
	local phorm ={}
	local rhormnew ={}
	local active = {}
	for i=1,#connh do
		rhormnew[i]= {}
		phorm[i] = {}
		active[i]= {}
	end

 	--Returns a table indicating wich hormones should be propagated through which faces
 	--according to the Propmodel and Direction


 	if(Propmodel == 'Attenuate') then
 		rhormnew,active = attenuateprop(rhorm) 
 	elseif(Propmodel=='Probability') then
 		local prob = 0.75
 		rhormnew,active = probprop(rhorm,prob)
 	else 
 		print('Hormone propagation Model not recognized')
 	end


 	if(Direction=='Forward')then
 		phorm = forwarddir(rhormnew,active,connh)
 	else
 		print('Hormone propagation direction Model not recognized')
 	end


 	return phorm,active -- With active and Rhorm we can make receivtable in the logger so that it shows which hormone was propagated through which face
end

function spatialtr(rhorm,connori,Genmodel)
	local rhormnew = {}
	for i=1,#rhorm do
		rhormnew[i]={}
	end
	local hormnew = {}


	--print('***************')
	
	for i=1,#rhorm do
		--print(i)
		if (#rhorm[i] > 0) then
			--print('++++++++++++++++++')
			--print(#rhorm[i])
			--print(connori[i])
			for j=1,#rhorm[i] do
				--print('zzzzzzzzzzzz')
				local exhorm = simUnpackFloatTable(rhorm[i][j])
				--for k,v in pairs(exhorm) do print(k,v) end
				if(Genmodel == 'baseHormone') then
					hormnew = baseHsptransform(exhorm,i,connori[i])
					--for k,v in pairs(hormnew) do print(k,v) end
				else
					--print('General Hormone Generation Model is not recognized')
				end
				rhormnew[i][j] =  simPackFloatTable(hormnew)
			end
		end
	end


	return rhormnew

end










